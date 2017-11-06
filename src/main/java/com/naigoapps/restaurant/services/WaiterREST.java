/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;
import com.naigoapps.restaurant.model.builders.WaiterBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.websocket.WebSocketServer;
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
@Path("/waiters")
@Produces(MediaType.APPLICATION_JSON)
public class WaiterREST {
    
    @Inject
    WaiterDao waiterDao;
    
    @Inject
    WebSocketServer server;
    
    @Inject
    DiningTableDao dtDao;
    
    @GET
    @Path("active")
    public Response getAvailableWaiters() {
        List<WaiterDTO> waiters = waiterDao.findActive().stream()
                .map(DTOAssembler::fromWaiter)
                .collect(Collectors.toList());
        
        return Response
                .status(200)
                .entity(waiters)
                .build();
    }
    
    @GET
    public Response getAllWaiters() {
        List<WaiterDTO> waiters = waiterDao.findAll().stream()
                .map(DTOAssembler::fromWaiter)
                .collect(Collectors.toList());
        
        
        return Response
                .status(200)
                .entity(waiters)
                .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWaiter(WaiterDTO w){
        Waiter waiter = new WaiterBuilder()
                .name(w.getName())
                .surname(w.getSurname())
                .cf(w.getCf())
                .status(w.getStatus())
                .getContent();
        waiterDao.persist(waiter);
        
        server.broadcastMessage("CREATED A WAITER");
        
        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromWaiter(waiter))
                .build();
    }
    
    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateWaiterName(@PathParam("uuid") String uuid, String name){
        Waiter w = waiterDao.findByUuid(uuid);
        w.setName(name);
        return Response.status(200).entity(DTOAssembler.fromWaiter(w)).build();
    }
    
    @PUT
    @Path("{uuid}/surname")
    @Transactional
    public Response updateWaiterSurname(@PathParam("uuid") String uuid, String surname){
        Waiter w = waiterDao.findByUuid(uuid);
        w.setSurname(surname);
        return Response.status(200).entity(DTOAssembler.fromWaiter(w)).build();
    }
    
    @PUT
    @Path("{uuid}/cf")
    @Transactional
    public Response updateWaiterCf(@PathParam("uuid") String uuid, String cf){
        Waiter w = waiterDao.findByUuid(uuid);
        w.setCf(cf);
        return Response.status(200).entity(DTOAssembler.fromWaiter(w)).build();
    }
    
    @PUT
    @Path("{uuid}/status")
    @Transactional
    public Response updateWaiterStatus(@PathParam("uuid") String uuid, String status){
        Waiter w = waiterDao.findByUuid(uuid);
        if(WaiterStatus.ACTIVE.equals(WaiterStatus.fromName(status)) || !hasTables(w)){
            w.setStatus(WaiterStatus.fromName(status));
            return Response.status(200).entity(DTOAssembler.fromWaiter(w)).build();
        }else{
            return Response.status(Response.Status.FORBIDDEN).entity("Il cameriere ha tavoli assegnati.").build();
        }
    }
        
    @DELETE
    @Transactional
    public Response deleteWaiter(String uuid){
        
        waiterDao.removeByUuid(uuid);
        
        return Response
                .status(Response.Status.OK)
                .build();
    }

    private boolean hasTables(Waiter w) {
        return !dtDao.findByWaiter(w.getUuid()).isEmpty();
    }

}
