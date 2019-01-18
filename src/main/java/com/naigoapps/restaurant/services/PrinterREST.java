/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.model.extra.QuantifiedOrders;
import com.naigoapps.restaurant.services.printing.services.PrintingService.Size;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jpos.FiscalPrinter;
import jpos.JposException;

/**
 *
 * @author naigo
 */
@Path("/printers")
@Produces(MediaType.APPLICATION_JSON)
public class PrinterREST {

    @Inject
    private SettingsDao sDao;

    @Inject
    private OrdinationDao ordDao;
    
    @Inject
    private DiningTableDao dtDao;

    @Inject
    private PrinterDao pDao;

    @Inject
    private LocationDao lDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPrinter(PrinterDTO p) {
        Printer printer = new Printer();
        printer.setName(p.getName());
        printer.setLineCharacters(p.getLineCharacters());
        pDao.persist(printer);

        return ResponseBuilder.created(DTOAssembler.fromPrinter(printer));
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updatePrinterName(@PathParam("uuid") String uuid, String name) {
        Printer p = pDao.findByUuid(uuid);
        if (p != null) {
            p.setName(name);
            return ResponseBuilder.ok(DTOAssembler.fromPrinter(p));
        }
        return ResponseBuilder.notFound("Stampante non trovata");
    }

    @PUT
    @Path("{uuid}/lineCharacters")
    @Transactional
    public Response updatePrinterLineCharacters(@PathParam("uuid") String uuid, int chars) {
        Printer target = pDao.findByUuid(uuid);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response deletePrinter(String uuid) {
        Printer p = pDao.findByUuid(uuid);
        if (p != null) {
            List<Location> locations = lDao.findByPrinter(p);
            if (locations.isEmpty()) {
                pDao.deleteByUuid(uuid);
                return ResponseBuilder.ok(uuid);
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
        Ordination ord = ordDao.findByUuid(ordinationUuid);
        List<Printer> printers = pDao.findAll();
        Map<Phase, QuantifiedOrders> phasesMap = new HashMap<>();
        Settings s = sDao.find();
        try {
            for (Printer p : printers) {
                PrintingService service = PrintingServiceProvider.get(p);
                phasesMap.clear();
                boolean needToPrint = false;
                for (Order order : ord.getOrders()) {
                    if (p.equals(order.getDish().getCategory().getLocation().getPrinter())) {
                        needToPrint = true;
                    }
                }
                if (needToPrint) {
                    for (Order order : ord.getOrders()) {
                        if (p.equals(order.getDish().getCategory().getLocation().getPrinter())
                                || !s.getShrinkOrdinations()) {
                            if (!phasesMap.containsKey(order.getPhase())) {
                                phasesMap.put(order.getPhase(), new QuantifiedOrders());
                            }
                            QuantifiedOrders phaseOrders = phasesMap.get(order.getPhase());
                            phaseOrders.addOrder(order);
                        }
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
            }
        } catch (IOException ex) {
            return ResponseBuilder.badRequest(ex.getMessage());
        }
        ord.setDirty(false);
        return ResponseBuilder.ok(DTOAssembler.fromOrdination(ord));

    }

    @POST
    @Path("abort")
    @Transactional
    public Response printOrdinationAbort(String ordinationUuid) throws PrintException {
        Ordination ord = ordDao.findByUuid(ordinationUuid);
        List<Printer> printers = pDao.findAll();
        try {
            for (Printer p : printers) {
                PrintingService service = PrintingServiceProvider.get(p);
                boolean needToPrint = false;
                for (Order order : ord.getOrders()) {
                    if (p.equals(order.getDish().getCategory().getLocation().getPrinter())) {
                        needToPrint = true;
                    }
                }
                if (needToPrint) {
                    service
                            .lf(5).size(Size.STANDARD)
                            .printCenter("ANNULLAMENTO")
                            .printCenter("Comanda " + ord.getProgressive() + " " + formatTime(ord.getCreationTime()))
                            .printCenter("Tavolo: " + ord.getTable().getTable().getName())
                            .printCenter("Cam. " + ord.getTable().getWaiter().getName())
                            .lf(5)
                            .cut()
                            .doPrint();
                }
            }
        } catch (IOException ex) {
            return ResponseBuilder.badRequest(ex.getMessage());
        }
        ord.setDirty(false);
        return ResponseBuilder.ok(DTOAssembler.fromOrdination(ord));
    }

    @PUT
    @Path("message")
    @Transactional
    public Response printMessage(@QueryParam("table") String tableUuid, @QueryParam("location") String locationUuid, String message) throws PrintException {
        DiningTable table = dtDao.findByUuid(tableUuid);
        Location location = lDao.findByUuid(locationUuid);
        if(location != null && table != null){
        try {
            Printer p = location.getPrinter();
            if(p != null){
                PrintingService service = PrintingServiceProvider.get(p);
                    service
                        .lf(5).size(Size.STANDARD)
                        .printCenter("MESSAGGIO")
                        .printCenter("Tavolo: " + table.getTable().getName())
                        .printCenter("Cam. " + table.getWaiter().getName())
                        .printCenter(message)
                        .lf(5)
                        .cut()
                        .doPrint();
                    return ResponseBuilder.ok();
            }else{
                return ResponseBuilder.badRequest("Postazione senza stampante");
            }
        } catch (IOException ex) {
            return ResponseBuilder.badRequest(ex.getMessage());
        }
        }else{
            return ResponseBuilder.badRequest("Tavolo o postazione non trovati");
        }
    }

    private PrintingService printPhases(PrintingService service, Map<Phase, QuantifiedOrders> phasesMap) throws IOException {
        List<Phase> usedPhases = new ArrayList<>(phasesMap.keySet());
        usedPhases.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));
        for (Phase p : usedPhases) {
            service.size(Size.STANDARD);
            service.printLeft(p.getName() + "...............");
            printPhaseOrders(service, phasesMap.get(p));
        }
        return service;
    }

    private PrintingService printPhaseOrders(PrintingService service, QuantifiedOrders orders) throws IOException {
        for (Order o : orders.getOrders().keySet()) {
            printOrder(service, o, orders.getOrders().get(o));
        }
        return service;
    }

    private PrintingService printOrder(PrintingService service, Order o, Integer quantity) throws IOException {
        if (service.getPrinter().equals(o.getDish().getCategory().getLocation().getPrinter())) {
            service.size(Size.STANDARD);
        } else {
            service.size(Size.SMALL);
        }
        service.printLeft(quantity + " " + o.getDish().getName());
        if (o.getAdditions().size() > 0) {
            for (Addition a : o.getAdditions()) {
                service.printLeft(a.getName() + " ");
            }
        }
        if (o.getNotes() != null && !o.getNotes().trim().isEmpty()) {
            service.printLeft(o.getNotes());
        }
        return service;
    }

    private String formatTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return date.format(formatter);
    }

    public void print() {
        FiscalPrinter p = new FiscalPrinter();
        try {
            p.open("printerName");
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.claim(100);
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.setDeviceEnabled(true);
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.beginFiscalDocument(1);
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.printFiscalDocumentLine("XXX");
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.endFiscalDocument();
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.close();
        } catch (JposException ex) {
            Logger.getLogger(PrinterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
