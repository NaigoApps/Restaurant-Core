/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.mobile;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.services.dto.mobile.mappers.EveningMapper;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;

/**
 *
 * @author naigo
 */
@Path("/mobile/evenings")
@Produces(MediaType.APPLICATION_JSON)
public class EveningREST {

    @Inject
    private EveningManager eveningManager;

    @Inject
    private EveningMapper eMapper;
    
    @GET
    @Path("selected")
    @Transactional
    public Response getSelectedEvening() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            return ResponseBuilder.ok(eMapper.map(e));
        } else {
            return ResponseBuilder.notFound("Nessuna serata selezionata");
        }
    }

}
