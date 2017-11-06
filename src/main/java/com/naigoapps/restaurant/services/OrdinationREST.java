/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.RequiredDish;
import com.naigoapps.restaurant.model.builders.OrdinationBuilder;
import com.naigoapps.restaurant.model.builders.RequiredDishBuilder;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.RequiredDishDao;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.RequiredDishDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    RequiredDishDao doDao;

    @Inject
    DishDao dDao;
    
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

    @POST
    @Transactional
    public Response createOrdination(RequiredDishDTO[] orders) {
        if (orders != null && orders.length > 0) {
            Evening e = eveningManager.getSelectedEvening();
            DiningTable table = e.getDiningTables().stream()
                    .filter(t -> t.getUuid().equals(orders[0].getTable()))
                    .findFirst()
                    .orElse(null);
            if (table != null) {
                Ordination ordination = null;
                synchronized (locks.ORDINATION_PROGRESSIVE()) {
                    ordination = new OrdinationBuilder()
                            .progressive(oDao.nextProgressive(LocalDate.now()))
                            .creationTime(LocalDateTime.now())
                            .table(table)
                            .getContent();
                    oDao.persist(ordination);
                    RequiredDishBuilder oBuilder = new RequiredDishBuilder();
                    List<RequiredDish> rd = new ArrayList<>();
                    for (RequiredDishDTO order : orders) {
                        for (int j = 0; j < order.getQuantity(); j++) {
                            RequiredDish o = oBuilder
                                    .dish(dDao.findByUuid(order.getDish()))
                                    .ordination(ordination)
                                    .price(order.getPrice())
                                    .getContent();
                            rd.add(o);
                            oDao.persist(o);
                        }
                    }
                    ordination.setOrders(rd);
                    printService.print(ordination.getUuid());
                }
                return Response.status(Response.Status.CREATED).entity(DTOAssembler.fromOrdination(ordination)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("dish")
    @Transactional
    public Response updateDish(@QueryParam("order") String orderUuid, @QueryParam("dish") String dishUuid) {
        Evening current = eveningManager.getSelectedEvening();
        RequiredDish order = doDao.findByUuid(orderUuid);
        Dish dish = dDao.findByUuid(dishUuid);
        if (current != null && order != null && dish != null && order.getOrdination().getTable().getEvening().equals(current)) {
            order.setDish(dish);
            order.setPrice(dish.getPrice());
            return Response.status(Response.Status.OK).entity(DTOAssembler.fromRequiredDish(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Transactional
    public Response deleteOrdination(String uuid){
        
        oDao.removeByUuid(uuid);
        
        return Response
                .status(Response.Status.OK)
                .build();
    }
}
