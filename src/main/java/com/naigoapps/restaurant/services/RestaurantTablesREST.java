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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.builders.RestaurantTableBuilder;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;
import com.naigoapps.restaurant.services.dto.mappers.RestaurantTableMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/restaurant-tables")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class RestaurantTablesREST {
    
    @Inject
    private RestaurantTableDao tablesDao;

    @Inject
    private EveningManager manager;
    
    @Inject
    private RestaurantTableMapper mapper;
    
    @GET
    public List<RestaurantTableDTO> getAllTables() {
        return tablesDao.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GET
    @Path("{uuid}")
    public RestaurantTableDTO find(@PathParam("uuid") String uuid) {
    	return mapper.map(tablesDao.findByUuid(uuid));
    }
    
    @GET
    @Path("available")
    public List<RestaurantTableDTO> getAvailableTables() {
        
        List<RestaurantTable> open = manager.getSelectedEvening().getDiningTables().stream()
                .filter(table -> table.getStatus().equals(DiningTableStatus.OPEN))
                .map(DiningTable::getTable)
                .collect(Collectors.toList());
        
        return tablesDao.findAll().stream()
        		.filter(table -> !open.contains(table))
                .map(mapper::map)
                .collect(Collectors.toList());
        
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createTable(){
        RestaurantTable table = new RestaurantTableBuilder()
                .getContent();
        
        tablesDao.persist(table);
        
        return table.getUuid();
    }
    
    @PUT
    @Path("{uuid}/name")
    public RestaurantTableDTO updateTableName(@PathParam("uuid") String uuid, String name){
        RestaurantTable rt = tablesDao.findByUuid(uuid);
        rt.setName(name);
        return mapper.map(rt);
    }
    
    @DELETE
    @Path("{uuid}")
    @Produces(MediaType.TEXT_PLAIN)
    public void deleteTable(@PathParam("uuid") String uuid){
        tablesDao.deleteByUuid(uuid);   
    }
}
