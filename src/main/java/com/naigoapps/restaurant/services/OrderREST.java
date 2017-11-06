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
import com.naigoapps.restaurant.model.RequiredDish;
import com.naigoapps.restaurant.model.builders.RequiredDishBuilder;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.model.dao.RequiredDishDao;
import com.naigoapps.restaurant.services.dto.RequiredDishDTO;
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
    RequiredDishDao rdDao;

    @Inject
    OrdinationDao oDao;

    @Inject
    DishDao dDao;

    @Inject
    PhaseDao pDao;

    @GET
    public Response getOrders() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            List<RequiredDishDTO> orders = new ArrayList<>();
            e.getDiningTables()
                    .forEach(dt -> dt.getOrdinations()
                        .forEach(o -> orders.addAll(
                        o.getOrders().stream()
                            .map(DTOAssembler::fromRequiredDish)
                            .collect(Collectors.toList()))));
            return Response.status(Response.Status.OK).entity(orders).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Transactional
    public Response createOrder(RequiredDishDTO dto) {
        if (dto.getOrdination() != null) {
            Evening e = eveningManager.getSelectedEvening();
            Ordination o = oDao.findByUuid(dto.getOrdination());
            if (o.getTable().getEvening().equals(e)) {
                RequiredDish order = new RequiredDishBuilder()
                        .ordination(o)
                        .dish(dDao.findByUuid(dto.getDish()))
                        .price(dto.getPrice())
                        .phase(pDao.findByUuid(dto.getPhase()))
                        .getContent();
                oDao.persist(order);
                return Response.status(Response.Status.CREATED).entity(DTOAssembler.fromRequiredDish(order)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("{uuid}/dish")
    @Transactional
    public Response updateDish(@PathParam("uuid") String orderUuid, String dishUuid) {
        Evening current = eveningManager.getSelectedEvening();
        RequiredDish order = rdDao.findByUuid(orderUuid);
        Dish dish = dDao.findByUuid(dishUuid);
        if (current != null && order != null && dish != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setDish(dish);
            order.setPrice(dish.getPrice());
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromRequiredDish(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{uuid}/price")
    @Transactional
    public Response updatePrice(@PathParam("uuid") String orderUuid, float price) {
        Evening current = eveningManager.getSelectedEvening();
        RequiredDish order = rdDao.findByUuid(orderUuid);
        if (current != null && order != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setPrice(price);
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromRequiredDish(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{uuid}/notes")
    @Transactional
    public Response updateNotes(@PathParam("uuid") String orderUuid, String notes) {
        Evening current = eveningManager.getSelectedEvening();
        RequiredDish order = rdDao.findByUuid(orderUuid);
        if (current != null && order != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setNotes(notes);
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromRequiredDish(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{uuid}/phase")
    @Transactional
    public Response updatePhase(@PathParam("uuid") String orderUuid, String phaseUuid) {
        Evening current = eveningManager.getSelectedEvening();
        RequiredDish order = rdDao.findByUuid(orderUuid);
        Phase phase = pDao.findByUuid(phaseUuid);
        if (current != null && order != null && phase != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setPhase(phase);
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromRequiredDish(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
