/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.UTEntry;
import com.naigoapps.restaurant.model.dao.UTDao;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/ut")
@Produces(MediaType.APPLICATION_JSON)
public class UsabilityTestingREST {

    @Inject
    UTDao utDao;

    @POST
    @Transactional
    public Response completeAction(@QueryParam("user") String user, @QueryParam("action") String action, String body) {

        UTEntry entry = new UTEntry();
        entry.setActor(user);
        entry.setActionName(action);
        entry.setActionBody(body);
        entry.setActionTime(LocalDateTime.now());
        
        utDao.persist(entry);
        
        return ResponseBuilder.ok();
    }

}
