/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.RequiredDish;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.WaiterBuilder;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPrinter(PrinterDTO p){
        Printer printer = new Printer();
        printer.setName(p.getName());
        pDao.persist(printer);
        
        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromPrinter(printer))
                .build();
    }
    
    @GET
    @Path("services")
    public Response getServices() {

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);

        List<String> printersList = new ArrayList<>();
        for (PrintService printerService : printServices) {
            printersList.add(printerService.getName());
        }

        return Response.ok().entity(printersList).build();
    }
    
    @GET
    @Path("printers")
    public Response getPrinters() {
        List<Printer> printers = pDao.findAll();

        return Response.ok().entity(printers).build();
    }

    @POST
    @Path("print")
    public Response print(String ordinationUuid) {

        Ordination ord = oDao.findByUuid(ordinationUuid);

        Map<Printer, StringBuilder> texts = new HashMap<>();
        for (RequiredDish dish : ord.getOrders()) {
            StringBuilder builder = texts.get(dish.getDish().getCategory().getLocation().getPrinter());
            if (builder == null) {
                builder = new StringBuilder();
                texts.put(dish.getDish().getCategory().getLocation().getPrinter(), builder);
            }
            builder.append("1 X ").append(dish.getDish().getName()).append("\n");
        }

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);

        for (Printer p : texts.keySet()) {
            PrintService service = findPrintService(p.getName(), printService);

            if (service != null) {
                DocPrintJob job = service.createPrintJob();

                try {

                    byte[] bytes;

                    // important for umlaut chars
                    bytes = texts.get(p).toString().getBytes("CP437");

                    Doc doc = new SimpleDoc(bytes, flavor, null);

                    job.print(doc, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return Response.ok().build();

    }

    public void printBytes(String printerName, byte[] bytes) {

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);
        PrintService service = findPrintService(printerName, printService);

        if (service != null) {
            DocPrintJob job = service.createPrintJob();

            try {

                Doc doc = new SimpleDoc(bytes, flavor, null);

                job.print(doc, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("NO SERVICES");
        }
    }

    private PrintService findPrintService(String printerName,
            PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }

        return null;
    }
}
