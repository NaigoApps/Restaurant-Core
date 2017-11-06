/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.RestaurantTableBuilder;
import com.naigoapps.restaurant.model.builders.WaiterBuilder;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
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
@Path("/restaurant-tables")
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantTablesREST {
    
    @Inject
    RestaurantTableDao tablesDao;

    @Inject
    EveningManager manager;
    
    @GET
    public Response getAllTables() {
        
        List<RestaurantTableDTO> data = tablesDao.findAll().stream()
                .map(DTOAssembler::fromRestaurantTable)
                .collect(Collectors.toList());
        
        return Response
                .status(200)
                .entity(data)
                .build();
    }
    
    @GET
    @Path("available")
    public Response getAvailableTables() {
        
        List<RestaurantTable> open = manager.getSelectedEvening().getDiningTables().stream()
                .filter(table -> !table.isClosed())
                .map(diningTable -> diningTable.getTable())
                .collect(Collectors.toList());
        
        List<RestaurantTableDTO> data = tablesDao.findAll().stream()
                .filter(table -> !open.contains(table))
                .map(DTOAssembler::fromRestaurantTable)
                .collect(Collectors.toList());
        
        return Response
                .status(200)
                .entity(data)
                .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTable(RestaurantTableDTO t){
        RestaurantTable table = new RestaurantTableBuilder()
                .name(t.getName())
                .getContent();
        tablesDao.persist(table);
        
        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromRestaurantTable(table))
                .build();
    }
    
    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateTableName(@PathParam("uuid") String uuid, String name){
        RestaurantTable rt = tablesDao.findByUuid(uuid);
        rt.setName(name);
        return Response.status(200).entity(DTOAssembler.fromRestaurantTable(rt)).build();
    }
    
    @DELETE
    @Transactional
    public Response deleteTable(String uuid){
        tablesDao.removeByUuid(uuid);   
        return Response
                .status(Response.Status.OK)
                .build();
    }
}
