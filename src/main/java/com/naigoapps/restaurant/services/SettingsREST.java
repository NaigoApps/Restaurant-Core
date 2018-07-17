/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsREST {

    @Inject
    SettingsDao settingsDao;

    @Inject
    PrinterDao pDao;
    
    @GET
    @Transactional
    public Response getSettings() {
        Settings settings = settingsDao.find();

        return Response
                .status(200)
                .entity(DTOAssembler.fromSettings(settings))
                .build();
    }
    
    @PUT
    @Path("client")
    @Transactional
    public Response setSettings(String value) {
        Settings settings = settingsDao.find();
        settings.setClientSettings(value);
        
        return Response.status(200).entity(DTOAssembler.fromSettings(settings)).build();
    }
    
    @PUT
    @Path("main-printer")
    @Transactional
    public Response setMainPrinter(String value) {
        return setPrinter((settings, printer) -> settings.setMainPrinter(printer), value);
    }
    
    @PUT
    @Path("fiscal-printer")
    @Transactional
    public Response setFiscalPrinter(String value) {
        return setPrinter((settings, printer) -> settings.setFiscalPrinter(printer), value);
    }
    
    public Response setPrinter(BiConsumer<Settings, Printer> setter, String value){
        Settings settings = settingsDao.find();
        
        Printer printer = pDao.findByUuid(value);
        if(printer != null){
            setter.accept(settings, printer);
            return ResponseBuilder.ok(DTOAssembler.fromSettings(settings));
        }else{
            return ResponseBuilder.notFound("Stampante non trovata");
        }
    }
}
