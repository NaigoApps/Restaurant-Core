/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;

/**
 *
 * @author naigo
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderREST {

    @Inject
    private EveningManager eveningManager;

    @Inject
    private OrderDao rdDao;

    @Inject
    private OrdinationDao oDao;

    @Inject
    private DishDao dDao;

    @Inject
    private PhaseDao pDao;

    @Inject
    private DiningTableDao dTDao;

    @GET
    @Transactional
    public Response getOrders() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            List<OrderDTO> orders = new ArrayList<>();
            e.getDiningTables().forEach(table -> 
                orders.addAll(table.listOrders().stream()
                        .map(DTOAssembler::fromOrder)
                        .collect(Collectors.toList())));
            return Response.status(Response.Status.OK).entity(orders).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("{tableUuid}")
    @Transactional
    public Response getOrders(@PathParam("tableUuid") String tableUuid) {
        Evening e = eveningManager.getSelectedEvening();
        DiningTable table = dTDao.findByUuid(tableUuid);
        if (e != null) {
            List<OrderDTO> orders = table.listOrders().stream()
                    .map(DTOAssembler::fromOrder)
                    .collect(Collectors.toList());
            return Response.status(Response.Status.OK).entity(orders).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

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
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrdination(order.getOrdination())).build();
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

    @DELETE
    @Transactional
    public Response deleteOrders(String[] orderUuids) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            List<Order> orders = Arrays.stream(orderUuids)
                    .map(uuid -> rdDao.findByUuid(uuid))
                    .collect(Collectors.toList());
            if (orders.stream().allMatch(order -> order.getOrdination().equals(orders.get(0).getOrdination()))) {
                if (orders.stream().allMatch(order -> order.getBill() == null)) {
                    Ordination targetOrdination = orders.get(0).getOrdination();
                    if (!targetOrdination.getOrders().stream()
                            .allMatch(orders::contains)) {
                        orders.forEach(order -> {
                            order.setOrdination(null);
                            rdDao.getEntityManager().remove(order);
                        });
                        if (targetOrdination.getOrders().isEmpty()) {
                            targetOrdination.setTable(null);
                            oDao.getEntityManager().remove(targetOrdination);
                        }

                        return ResponseBuilder.ok(DTOAssembler.fromOrdination(targetOrdination));
                    } else {
                        return ResponseBuilder.badRequest("La comanda verrebbe eliminata");
                    }
                }
                return ResponseBuilder.badRequest("Alcuni ordini hanno conti associati");
            }
            return ResponseBuilder.badRequest("Ordini non provenienti dalla stessa comanda");
        }
        return ResponseBuilder.badRequest("Serata non correttamente selezionata");
    }

}
