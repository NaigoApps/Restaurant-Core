/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import com.naigoapps.restaurant.services.dto.mappers.WaiterMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Accessible
@Transactional
@Path("/waiters")
@Produces(MediaType.APPLICATION_JSON)
public class WaiterREST {

    @Inject
    private WaiterDao waiterDao;

    @Inject
    private DiningTableDao dtDao;

    @Inject
    private WaiterMapper mapper;
    
    @GET
    @Path("active")
    public List<WaiterDTO> getAvailableWaiters() {
        return waiterDao.findActive().stream()
        		.filter(w -> WaiterStatus.REMOVED != w.getStatus())
        		.filter(w -> WaiterStatus.SUSPENDED != w.getStatus())
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @GET
    public List<WaiterDTO> getAllWaiters() {
        return waiterDao.findAll().stream()
                .filter(w -> WaiterStatus.REMOVED != w.getStatus())
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GET
    @Path("{uuid}")
    public WaiterDTO findByUuid(@PathParam("uuid") String uuid) {
    	return mapper.map(waiterDao.findByUuid(uuid));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWaiter() {
        Waiter waiter = new Waiter();
        waiterDao.persist(waiter);
        return waiter.getUuid();
    }

    @PUT
    @Path("{uuid}/name")
    public WaiterDTO updateWaiterName(@PathParam("uuid") String uuid, String name) {
        Waiter w = waiterDao.findByUuid(uuid);
        w.setName(name);
        return mapper.map(w);
    }

    @PUT
    @Path("{uuid}/surname")
    public WaiterDTO updateWaiterSurname(@PathParam("uuid") String uuid, String surname) {
        Waiter w = waiterDao.findByUuid(uuid);
        w.setSurname(surname);
        return mapper.map(w);
    }

    @PUT
    @Path("{uuid}/cf")
    public WaiterDTO updateWaiterCf(@PathParam("uuid") String uuid, String cf) {
        Waiter w = waiterDao.findByUuid(uuid);
        w.setCf(cf);
        return mapper.map(w);
    }

    @PUT
    @Path("{uuid}/status")
    public WaiterDTO updateWaiterStatus(@PathParam("uuid") String uuid, String statusText) {
        Waiter w = waiterDao.findByUuid(uuid);
        WaiterStatus status = WaiterStatus.valueOf(statusText);
        if (status != WaiterStatus.REMOVED || !hasTables(w)) {
            w.setStatus(status);
            return mapper.map(w);
        } else {
            throw new BadRequestException("Il cameriere ha tavoli assegnati.");
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{uuid}")
    public void deleteWaiter(@PathParam("uuid") String uuid) {
        waiterDao.deleteByUuid(uuid);
    }

    private boolean hasTables(Waiter w) {
        return !dtDao.findByWaiter(w.getUuid()).isEmpty();
    }

}
