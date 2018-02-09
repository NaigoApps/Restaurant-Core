/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.print.PrintException;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("/ordinations")
@Produces(MediaType.APPLICATION_JSON)
public class OrdinationREST {

    @Inject
    Locks locks;

    @Inject
    EveningManager eveningManager;

    @Inject
    OrdinationDao oDao;

    @Inject
    OrderDao doDao;

    @Inject
    DishDao dDao;

    @Inject
    PhaseDao pDao;

    @Inject
    AdditionDao aDao;

    @Inject
    PrinterREST printService;

    @GET
    public Response getOrdinations() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            List<OrdinationDTO> ordinations = new ArrayList<>();
            e.getDiningTables()
                    .forEach(dt -> ordinations.addAll(
                    dt.getOrdinations().stream()
                            .map(DTOAssembler::fromOrdination)
                            .collect(Collectors.toList())));
            return Response.status(Response.Status.OK).entity(ordinations).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{uuid}/abort")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response sendOrdinationAbort(@PathParam("uuid") String ordinationUuid) {
        Evening e = eveningManager.getSelectedEvening();
        Ordination ordination = oDao.findByUuid(ordinationUuid, Ordination.class);
        if (ordination != null) {
            try {
                printService.printOrdinationAbort(ordinationUuid);
                return Response.status(Response.Status.OK).entity(ordinationUuid).build();
            } catch (PrintException ex) {
                return ResponseBuilder.badRequest("Impossibile stampare");
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{uuid}/orders")
    @Transactional
    public Response editOrders(@PathParam("uuid") String ordinationUuid, OrderDTO[] orders) {
        if (orders != null) {
            Evening e = eveningManager.getSelectedEvening();
            Ordination ordination = oDao.findByUuid(ordinationUuid, Ordination.class);
            if (ordination != null) {
                ordination.getOrders().forEach(o -> {
                    o.setOrdination(null);
                    o.clearAdditions();
                    oDao.delete(o);
                });
                ordination.getOrders().clear();
                OrderBuilder oBuilder = new OrderBuilder();
                List<Order> rd = new ArrayList<>();
                for (OrderDTO order : orders) {
                    Order o = oBuilder
                            .dish(dDao.findByUuid(order.getDish()))
                            .ordination(ordination)
                            .price(order.getPrice())
                            .phase(pDao.findByUuid(order.getPhase()))
                            .getContent();
                    List<Addition> additions = new ArrayList<>();
                    order.getAdditions().stream().forEach(additionUuid -> {
                        Addition addition = aDao.findByUuid(additionUuid);
                        additions.add(addition);
                    });
                    o.setAdditions(additions);
                    oDao.persist(o);
                    rd.add(o);
                }
                ordination.setOrders(rd);
                ordination.setDirty(true);
                return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrdination(ordination)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Transactional
    public Response deleteOrdination(String uuid) {

        oDao.removeByUuid(uuid);

        return Response
                .status(Response.Status.OK)
                .build();
    }
}
