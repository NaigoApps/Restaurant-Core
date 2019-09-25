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
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.mappers.OrderMapper;

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
    private DiningTableDao dTDao;
    
    @Inject
    private OrderMapper mapper;

    @GET
    @Transactional
    public List<OrderDTO> getOrders() {
        Evening e = eveningManager.getSelectedEvening();
        List<OrderDTO> orders = new ArrayList<>();
        e.getDiningTables().forEach(table -> 
            orders.addAll(table.getOrders().stream()
                    .map(mapper::map)
                    .collect(Collectors.toList())));
        return orders;
    }

    @GET
    @Path("{tableUuid}")
    @Transactional
    public List<OrderDTO> getOrders(@PathParam("tableUuid") String tableUuid) {
        DiningTable table = dTDao.findByUuid(tableUuid);
        return table.getOrders().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("{uuid}/price")
    @Transactional
    public void updatePrice(@PathParam("uuid") String orderUuid, float price) {
        rdDao.findByUuid(orderUuid).setPrice(price);
    }

    @DELETE
    @Transactional
    public void deleteOrders(String[] orderUuids) {
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
                } else {
                	throw new BadRequestException("La comanda verrebbe eliminata");
                }
            }
            throw new BadRequestException("Alcuni ordini hanno conti associati");
        }
        throw new BadRequestException("Ordini non provenienti dalla stessa comanda");
    }

}
