/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.LocationDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
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
    public Response createLocation(LocationDTO l){
        Location location = new Location();
        location.setName(l.getName());
        location.setPrinter(pDao.findByUuid(l.getPrinter()));
        lDao.persist(location);
        
        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromLocation(location))
                .build();
    }
    
}
