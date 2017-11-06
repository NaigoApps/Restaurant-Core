/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.DishStatus;
import com.naigoapps.restaurant.model.builders.CategoryBuilder;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.PhaseDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/phases")
@Produces(MediaType.APPLICATION_JSON)
public class PhaseREST {
    
    @Inject
    PhaseDao pDao;
    
    @GET
    public Response findAll() {
        List<PhaseDTO> phases = pDao.findAll().stream()
                .map(p -> DTOAssembler.fromPhase(p))
                .collect(Collectors.toList());
        
        return Response
                .status(200)
                .entity(phases)
                .build();
    }
    
}
