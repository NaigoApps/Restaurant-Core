/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.DishStatus;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/dishes-statuses")
@Produces(MediaType.APPLICATION_JSON)
public class DishStatusREST {
    
    @GET
    public Response findAll() {
        DishStatus[] statuses = DishStatus.values();
        
        return Response
                .status(200)
                .entity(statuses)
                .build();
    }
    
}
