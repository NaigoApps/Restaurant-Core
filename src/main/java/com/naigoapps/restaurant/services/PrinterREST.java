/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.extra.QuantifiedOrders;
import com.naigoapps.restaurant.services.PrinterService.Align;
import com.naigoapps.restaurant.services.PrinterService.Size;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/printers")
@Produces(MediaType.APPLICATION_JSON)
public class PrinterREST {

    @Inject
    OrdinationDao oDao;

    @Inject
    PrinterDao pDao;

    @Inject
    LocationDao lDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPrinter(PrinterDTO p) {
        Printer printer = new Printer();
        printer.setName(p.getName());
        pDao.persist(printer);

        return ResponseBuilder.created(DTOAssembler.fromPrinter(printer));
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updatePrinterName(@PathParam("uuid") String uuid, String name) {
        Printer p = pDao.findByUuid(uuid, Printer.class);
        if (p != null) {
            p.setName(name);
            return ResponseBuilder.ok(DTOAssembler.fromPrinter(p));
        }
        return ResponseBuilder.notFound("Stampante non trovata");
    }

    @PUT
    @Path("{uuid}/main")
    @Transactional
    public Response updateMainPrinter(@PathParam("uuid") String uuid, boolean main) {
        Printer target = pDao.findByUuid(uuid, Printer.class);
        if (target != null) {
            target.setMain(main);
        }

        return ResponseBuilder.ok(DTOAssembler.fromPrinter(target));
    }

    @PUT
    @Path("{uuid}/lineCharacters")
    @Transactional
    public Response updatePrinterLineCharacters(@PathParam("uuid") String uuid, int chars) {
        Printer target = pDao.findByUuid(uuid, Printer.class);
        if (target != null) {
            target.setLineCharacters(chars);
        }

        return ResponseBuilder.ok(DTOAssembler.fromPrinter(target));
    }

    @GET
    @Path("services")
    public Response getPrintServices() {
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(flavor, attributes);

        List<String> printersList = Arrays.stream(printServices)
                .map(service -> service.getName())
                .collect(Collectors.toList());

        return ResponseBuilder.ok(printersList);
    }

    @GET
    @Path("printers")
    public Response getPrinters() {
        List<Printer> printers = pDao.findAll();
        return ResponseBuilder.ok(printers);
    }

    @DELETE
    @Transactional
    public Response deletePrinter(String uuid) {
        Printer p = pDao.findByUuid(uuid, Printer.class);
        if (p != null) {
            List<Location> locations = lDao.findByPrinter(p);
            if (locations.isEmpty()) {
                pDao.deleteByUuid(uuid, Printer.class);
                return ResponseBuilder.ok();
            } else if (locations.size() > 1) {
                return ResponseBuilder.badRequest("Stampante utilizzata nelle postazioni "
                        + locations.stream().map(l -> l.getName()).collect(Collectors.joining(", ")));
            } else {
                return ResponseBuilder.badRequest("Stampante utilizzata nella postazione " + locations.get(0).getName());
            }
        }
        return ResponseBuilder.notFound("Stampante non trovata");
    }

    @POST
    @Path("print")
    @Transactional
    public Response printOrdination(String ordinationUuid) throws PrintException {
        Ordination ord = oDao.findByUuid(ordinationUuid, Ordination.class);

        Map<Printer, List<Order>> printersOrders = new HashMap<>();
        Map<Phase, QuantifiedOrders> phasesMap = new HashMap<>();
        ord.getOrders().forEach((dish) -> {
            Printer p = dish.getDish().getCategory().getLocation().getPrinter();
            if (!printersOrders.containsKey(p)) {
                printersOrders.put(p, new ArrayList<>());
            }
            printersOrders.get(p).add(dish);
        });
        try {
            for (Printer p : printersOrders.keySet()) {
                PrinterService service = new PrinterService(p);
                for (Order order : printersOrders.get(p)) {
                    if (!phasesMap.containsKey(order.getPhase())) {
                        phasesMap.put(order.getPhase(), new QuantifiedOrders());
                    }
                    QuantifiedOrders phaseOrders = phasesMap.get(order.getPhase());
                    phaseOrders.addOrder(order);
                }
                service
                        .lf(3).size(Size.STANDARD)
                        .printCenter("Comanda " + ord.getProgressive() 
                                + " Tavolo: " + ord.getTable().getTable().getName())
                        .printCenter(formatTime(ord.getCreationTime()))
                        .printCenter("Cam. " + ord.getTable().getWaiter().getName());
                printPhases(service, phasesMap)
                        .lf(6)
                        .cut()
                        .doPrint();
            }
        } catch (IOException ex) {
            return ResponseBuilder.badRequest(ex.getMessage());
        }
        ord.setDirty(false);
        return ResponseBuilder.ok(DTOAssembler.fromOrdination(ord));

    }

    private PrinterService printPhases(PrinterService service, Map<Phase, QuantifiedOrders> phasesMap) throws IOException {
        List<Phase> usedPhases = new ArrayList<>(phasesMap.keySet());
        usedPhases.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        for (Phase p : usedPhases) {
            service.printLeft(p.getName() + "...............");
            printPhaseOrders(service, phasesMap.get(p));
        }
        return service;
    }

    private PrinterService printPhaseOrders(PrinterService service, QuantifiedOrders orders) throws IOException {
        for (Order o : orders.getOrders().keySet()) {
            printOrder(service, o, orders.getOrders().get(o));
        }
        return service;
    }

    private PrinterService printOrder(PrinterService service, Order o, Integer quantity) throws IOException {
        service.printLeft(quantity + " " + o.getDish().getName());
        if (o.getAdditions().size() > 0) {
            for (Addition a : o.getAdditions()) {
                service.printLeft(a.getName() + " ");
            }
        }
        if (o.getNotes() != null) {
            service.printLeft(o.getNotes());
        }
        return service;
    }

    private String formatTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return date.format(formatter);
    }
}
