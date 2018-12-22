/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.builders.OrdinationBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.print.PrintException;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

    @Inject
    private Locks locks;

    @Inject
    private EveningManager eveningManager;

    @Inject
    private OrdinationDao ordDao;

    @Inject
    private OrderDao oDao;

    @Inject
    private DishDao dDao;

    @Inject
    private DiningTableDao dTDao;

    @Inject
    private PhaseDao pDao;

    @Inject
    private AdditionDao aDao;

    @Inject
    private PrinterREST printService;

    @POST
    @Transactional
    public Response createOrdination(@QueryParam("table") String tableUuid, OrdinationDTO ordDto) {
        if (tableUuid != null && ordDto != null
                && ordDto.getOrders() != null && !ordDto.getOrders().isEmpty()) {
            Evening e = eveningManager.getSelectedEvening();
            DiningTable table = dTDao.findByUuid(tableUuid);
            if (table != null && table.getEvening().equals(e)) {
                Ordination ordination;
                synchronized (locks.ORDINATION_PROGRESSIVE()) {
                    ordination = new OrdinationBuilder()
                            .progressive(ordDao.nextProgressive(e))
                            .creationTime(LocalDateTime.now())
                            .table(table)
                            .dirty(true)
                            .getContent();
                    oDao.persist(ordination);
                    persistOrders(ordination, ordDto.getOrders());
                    if(table.getBills().isEmpty()){
                        table.setStatus(DiningTableStatus.OPEN);
                    }else{
                        table.setStatus(DiningTableStatus.CLOSING);
                    }
                }
                return Response.status(Response.Status.CREATED).entity(DTOAssembler.fromOrdination(ordination)).build();
            }
            return ResponseBuilder.notFound(TABLE_NOT_FOUND);
        }
        return ResponseBuilder.badRequest("tavolo o ordini non validi");
    }

    private void persistOrders(Ordination ordination, List<OrderDTO> orders) {
        OrderBuilder oBuilder = new OrderBuilder();
        for (OrderDTO order : orders) {
            Order o = oBuilder
                    .dish(dDao.findByUuid(order.getDish()))
                    .ordination(ordination)
                    .price(order.getPrice())
                    .phase(order.getPhase() != null ? pDao.findByUuid(order.getPhase()) : pDao.findAll().get(0))
                    .notes(order.getNotes())
                    .getContent();
            List<Addition> additions = new ArrayList<>();
            order.getAdditions().stream().forEach(additionUuid -> {
                Addition addition = aDao.findByUuid(additionUuid);
                additions.add(addition);
            });
            o.setAdditions(additions);
            oDao.persist(o);
        }
    }

    @GET
    @Transactional
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

    @GET
    @Path("{tableUuid}")
    @Transactional
    public Response getOrdinations(@PathParam("tableUuid") String tableUuid) {
        Evening e = eveningManager.getSelectedEvening();
        DiningTable table = dTDao.findByUuid(tableUuid);
        if (e != null) {
            List<OrdinationDTO> ordinations = table.getOrdinations().stream()
                    .map(DTOAssembler::fromOrdination)
                    .collect(Collectors.toList());
            return Response.status(Response.Status.OK).entity(ordinations).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{uuid}")
    @Transactional
    public Response editOrdination(@PathParam("uuid") String ordinationUuid, OrdinationDTO dto) {
        if (dto != null) {
            Ordination ordination = ordDao.findByUuid(ordinationUuid);
            if (ordination != null) {
                List<Order> oldOrders = new ArrayList<>(ordination.getOrders());
                List<Order> newOrders = buildOrders(dto.getOrders());

                List<Order> oldFixedOrders = oldOrders.stream()
                        .filter(order -> order.getBill() != null)
                        .collect(Collectors.toList());

                for (Order requiredOrder : oldFixedOrders) {
                    Order newMatchingOrder = findMatchingOrder(newOrders, requiredOrder);
                    if (newMatchingOrder != null) {
                        newMatchingOrder.setBill(requiredOrder.getBill());
                    } else {
                        return ResponseBuilder.badRequest("Impossibile modificare ordini associati ad un conto");
                    }
                }

                ordination.getOrders().forEach(o -> {
                    if (o.getBill() != null) {
                        o.getBill().removeOrder(o);
                    }
                    o.clearAdditions();
                    oDao.delete(o);
                });
                ordination.clearOrders();

                for (Order order : newOrders) {
                    order.setOrdination(ordination);
                    oDao.persist(order);
                }
                ordination.setDirty(true);
                ordination.getTable().updateStatus();
                return Response.status(Response.Status.OK).entity(DTOAssembler.fromOrdination(ordination)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private Order findMatchingOrder(List<Order> orders, Order target) {
        for (Order order : orders) {
            if (order.isTheSame(target) && order.getBill() == null) {
                return order;
            }
        }
        return null;
    }

    private List<Order> buildOrders(List<OrderDTO> dtos) {
        OrderBuilder builder = new OrderBuilder();
        List<Order> result = new ArrayList<>();
        for (OrderDTO order : dtos) {
            Order o = builder
                    .dish(dDao.findByUuid(order.getDish()))
                    .price(order.getPrice())
                    .phase(pDao.findByUuid(order.getPhase()))
                    .notes(order.getNotes())
                    .getContent();
            List<Addition> additions = new ArrayList<>();
            order.getAdditions().stream().forEach(additionUuid -> {
                Addition addition = aDao.findByUuid(additionUuid);
                additions.add(addition);
            });
            o.setAdditions(additions);
            result.add(o);
        }
        return result;
    }

    @PUT
    @Path("{uuid}/abort")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response sendOrdinationAbort(@PathParam("uuid") String ordinationUuid) {
        Evening e = eveningManager.getSelectedEvening();
        Ordination ordination = ordDao.findByUuid(ordinationUuid);
        if (e != null && ordination != null && e.equals(ordination.getTable().getEvening())) {
            try {
                printService.printOrdinationAbort(ordinationUuid);
                return Response.status(Response.Status.OK).entity(ordinationUuid).build();
            } catch (PrintException ex) {
                return ResponseBuilder.badRequest("Impossibile stampare");
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public Response deleteOrdination(@PathParam("uuid") String ordUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        Ordination ordination = ordDao.findByUuid(ordUuid);
        if(ordination != null){
            DiningTable table = ordination.getTable();
            if (currentEvening != null && currentEvening.equals(ordination.getTable().getEvening())) {
                DiningTable targetTable = ordination.getTable();
                List<Order> orders = ordination.getOrders();
                if (orders.stream().allMatch(order -> order.getBill() == null)) {
                    ordination.setTable(null);
                    ordination.clearOrders();

                    oDao.getEntityManager().remove(ordination);
                    orders.forEach(order -> oDao.getEntityManager().remove(order));
                    table.updateStatus();
                    return ResponseBuilder.ok(DTOAssembler.fromDiningTable(targetTable));
                }
                return ResponseBuilder.badRequest("Alcuni ordini hanno conti associati");
            }
        }else{
            return ResponseBuilder.badRequest("Ordinazione non trovata");
        }
        return ResponseBuilder.badRequest("Serata non correttamente selezionata");
    }
}
