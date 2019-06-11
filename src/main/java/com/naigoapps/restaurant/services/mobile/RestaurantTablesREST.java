/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.mobile;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.services.dto.mobile.RestaurantTableDTO;
import com.naigoapps.restaurant.services.dto.mobile.mappers.RestaurantTableMapper;

/**
 *
 * @author naigo
 */
@Path("mobile/restaurant-tables")
@Produces(MediaType.APPLICATION_JSON)
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
    
}
