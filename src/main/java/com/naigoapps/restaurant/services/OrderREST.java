/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderREST {

    @Inject
    EveningManager eveningManager;

    @Inject
    OrderDao rdDao;

    @Inject
    OrdinationDao oDao;

    @Inject
    DishDao dDao;

    @Inject
    PhaseDao pDao;

    @PUT
    @Path("{uuid}/dish")
    @Transactional
    public Response updateDish(@PathParam("uuid") String orderUuid, String dishUuid) {
        Evening current = eveningManager.getSelectedEvening();
        Order order = rdDao.findByUuid(orderUuid);
        Dish dish = dDao.findByUuid(dishUuid);
        if (current != null && order != null && dish != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setDish(dish);
            order.setPrice(dish.getPrice());
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrder(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{uuid}/price")
    @Transactional
    public Response updatePrice(@PathParam("uuid") String orderUuid, float price) {
        Evening current = eveningManager.getSelectedEvening();
        Order order = rdDao.findByUuid(orderUuid);
        if (current != null && order != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setPrice(price);
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrder(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{uuid}/notes")
    @Transactional
    public Response updateNotes(@PathParam("uuid") String orderUuid, String notes) {
        Evening current = eveningManager.getSelectedEvening();
        Order order = rdDao.findByUuid(orderUuid);
        if (current != null && order != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setNotes(notes);
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrder(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{uuid}/phase")
    @Transactional
    public Response updatePhase(@PathParam("uuid") String orderUuid, String phaseUuid) {
        Evening current = eveningManager.getSelectedEvening();
        Order order = rdDao.findByUuid(orderUuid);
        Phase phase = pDao.findByUuid(phaseUuid);
        if (current != null && order != null && phase != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setPhase(phase);
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrder(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
