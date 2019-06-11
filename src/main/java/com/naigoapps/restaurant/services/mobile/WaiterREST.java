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

import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.mobile.WaiterDTO;
import com.naigoapps.restaurant.services.dto.mobile.mappers.WaiterMapper;

/**
 *
 * @author naigo
 */
@Path("/mobile/waiters")
@Produces(MediaType.APPLICATION_JSON)
public class WaiterREST {

    @Inject
    private WaiterDao waiterDao;
    
    @Inject
    private WaiterMapper mapper;

    @GET
    public List<WaiterDTO> getAvailableWaiters() {
        return waiterDao.findActive().stream()
        		.map(mapper::map)
                .collect(Collectors.toList());
    }

}
