/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.LocationDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
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
@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
public class LocationREST {

    @Inject
    LocationDao lDao;

    @Inject
    PrinterDao pDao;

    @Inject
    EveningManager manager;

    @GET
    public Response getLocations() {

        List<LocationDTO> data = lDao.findAll().stream()
                .map(DTOAssembler::fromLocation)
                .collect(Collectors.toList());

        return Response
                .status(200)
                .entity(data)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createLocation(LocationDTO l) {
        Printer printer = pDao.findByUuid(l.getPrinter(), Printer.class);
        if (printer != null) {
            Location location = new Location();
            location.setName(l.getName());
            location.setPrinter(printer);
            lDao.persist(location);

            return ResponseBuilder.created(DTOAssembler.fromLocation(location));
        } else {
            return ResponseBuilder.notFound("Stampante non trovata");
        }
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateLocationName(@PathParam("uuid") String uuid, String name) {
        Location l = lDao.findByUuid(uuid, Location.class);
        if (l != null) {
            l.setName(name);
            return ResponseBuilder.ok(DTOAssembler.fromLocation(l));
        } else {
            return ResponseBuilder.notFound("Postazione non trovata");
        }
    }

    @PUT
    @Path("{uuid}/printer")
    @Transactional
    public Response updateLocationPrinter(@PathParam("uuid") String uuid, String printer) {
        Location l = lDao.findByUuid(uuid, Location.class);
        Printer p = pDao.findByUuid(printer, Printer.class);
        if (l != null) {
            if (p != null) {
                l.setPrinter(p);
                return ResponseBuilder.ok(DTOAssembler.fromLocation(l));
            } else {
                return ResponseBuilder.notFound("Stampante non trovata");
            }
        } else {
            return ResponseBuilder.notFound("Postazione non trovata");
        }
    }
    
    @DELETE
    @Transactional
    public Response deleteTable(String uuid){
        lDao.removeByUuid(uuid);   
        return Response
                .status(Response.Status.OK)
                .build();
    }

}
