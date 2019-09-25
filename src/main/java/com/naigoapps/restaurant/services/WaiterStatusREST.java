/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.WaiterStatus;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/waiter-statuses")
@Produces(MediaType.APPLICATION_JSON)
public class WaiterStatusREST {

    @GET
    public WaiterStatus[] findAll() {
        return WaiterStatus.values();
    }

}
