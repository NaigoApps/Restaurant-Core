/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.PhaseDTO;
import com.naigoapps.restaurant.services.dto.mappers.PhaseMapper;

/**
 *
 * @author naigo
 */
@Path("/phases")
@Produces(MediaType.APPLICATION_JSON)
public class PhaseREST {
    
    @Inject
    private PhaseDao pDao;
    
    @Inject
    private PhaseMapper mapper;
    
    @GET
    public List<PhaseDTO> findAll() {
        return pDao.findAll().stream()
        	.map(mapper::map)
        	.collect(Collectors.toList());
    }
    
}
