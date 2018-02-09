/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.builders.OrdinationBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.BillDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.printing.PartialBillPrinter;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/dining-tables")
@Produces(MediaType.APPLICATION_JSON)
public class DiningTableREST {

    @Inject
    Locks locks;

    @Inject
    EveningManager eveningManager;

    @Inject
    WaiterDao wDao;

    @Inject
    RestaurantTableDao rtDao;

    @Inject
    DiningTableDao dtDao;

    @Inject
    DishDao dDao;

    @Inject
    PhaseDao pDao;

    @Inject
    AdditionDao aDao;

    @Inject
    OrdinationDao ordDao;

    @Inject
    OrderDao oDao;

    @Inject
    PrinterDao prDao;

    @Inject
    BillDao bDao;

    @GET
    @Transactional
    public Response getByEvening() {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            List<DiningTableDTO> result = currentEvening.getDiningTables()
                    .stream()
                    .map(DTOAssembler::fromDiningTable)
                    .collect(Collectors.toList());
            return Response.ok().entity(result).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("{uuid}/ordinations")
    @Transactional
    public Response createOrdination(@PathParam("uuid") String tableUuid, OrderDTO[] orders) {
        if (tableUuid != null && orders != null && orders.length > 0) {
            Evening e = eveningManager.getSelectedEvening();
            DiningTable table = e.getDiningTables().stream()
                    .filter(t -> t.getUuid().equals(tableUuid))
                    .findFirst()
                    .orElse(null);
            if (table != null) {
                Ordination ordination;
                synchronized (locks.ORDINATION_PROGRESSIVE()) {
                    ordination = new OrdinationBuilder()
                            .progressive(ordDao.nextProgressive(e))
                            .creationTime(LocalDateTime.now())
                            .table(table)
                            .dirty(true)
                            .getContent();
                    oDao.persist(ordination);
                    OrderBuilder oBuilder = new OrderBuilder();
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
                    }
                }
                return Response.status(Response.Status.CREATED).entity(
                        new Object[]{DTOAssembler.fromDiningTable(table), ordination.getUuid()}
                ).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private DiningTable findTableInEvening(Evening e, String uuid) {
        return e.getDiningTables().stream()
                .filter(table -> table.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @PUT
    @Path("{uuid}/cover-charges")
    @Transactional
    public Response updateCoverCharges(@PathParam(value = "uuid") String tableUuid, int coverCharges) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                toUpdate.setCoverCharges(coverCharges);
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{uuid}/waiter")
    @Transactional
    public Response updateWaiter(@PathParam("uuid") String tableUuid, String waiterUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                toUpdate.setWaiter(wDao.findByUuid(waiterUuid));
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{uuid}/table")
    @Transactional
    public Response updateTable(@PathParam("uuid") String diningTableUuid, String tableUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, diningTableUuid);
            if (toUpdate != null) {
                toUpdate.setTable(rtDao.findByUuid(tableUuid));
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("{uuid}/bills")
    @Transactional
    public Response removeBill(@PathParam("uuid") String tableUuid, String billUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            Bill bill = bDao.findByUuid(billUuid, Bill.class);
            if (toUpdate != null && bill != null && toUpdate.equals(bill.getTable())) {
                bill.clearOrders();
                bill.setTable(null);
                bDao.getEntityManager().remove(bill);
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return ResponseBuilder.notFound("Tavolo o scontrino non trovato");
            }
        } else {
            return ResponseBuilder.notFound("Serata non selezionata");
        }
    }

    @PUT
    @Path("{uuid}/bills")
    @Transactional
    public Response addBill(@PathParam("uuid") String tableUuid, String[] ordersUuids) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                List<Order> orders = Arrays.stream(ordersUuids)
                        .map(uuid -> oDao.findByUuid(uuid, Order.class))
                        .collect(Collectors.toList());
                if (orders.stream().allMatch(order -> toUpdate.equals(order.getOrdination().getTable()))) {
                    if (orders.stream().allMatch(order -> order.getBill() == null)) {
                        Bill bill = new BillBuilder()
                                .progressive(bDao.nextProgressive(currentEvening.getDay()))
                                .table(toUpdate)
                                .orders(orders)
                                .getContent();
                        dtDao.persist(bill);
                        return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
                    } else {
                        return ResponseBuilder.badRequest("Vi sono ordini già chiusi");
                    }
                } else {
                    return ResponseBuilder.badRequest("Vi sono ordini non appartenenti al tavolo");
                }
            } else {
                return ResponseBuilder.notFound("Tavolo non trovato");
            }
        } else {
            return ResponseBuilder.notFound("Serata non selezionata");
        }
    }

    @DELETE
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteDiningTable(String uuid) {

        DiningTable toRemove = dtDao.findByUuid(uuid);
        if (toRemove != null) {
            if (toRemove.getBills().size() > 0) {
                return ResponseBuilder.badRequest("Al tavolo sono collegati degli scontrini");
            }
            if (toRemove.getOrdinations().size() > 0) {
                return ResponseBuilder.badRequest("Il tavolo contiene delle comande");
            }
            dtDao.removeByUuid(uuid);
            return ResponseBuilder.ok(uuid);
        } else {
            return ResponseBuilder.badRequest("Tavolo non trovato");
        }

    }

    @POST
    @Path("print-partial-bill")
    @Transactional
    public Response printPartialBill(String diningTableUuid) {
        DiningTable table = dtDao.findByUuid(diningTableUuid, DiningTable.class);
        if (table != null) {
            Printer mainPrinter = prDao.findMainPrinter();
            if (mainPrinter != null) {
                try {
                    PrinterService service = new PrinterService(mainPrinter);
                    service.accept(new PartialBillPrinter(), table);
                    service.lf(3);
                    service.cut();
                    service.doPrint();
                    return ResponseBuilder.ok();
                } catch (IOException ex) {
                    return ResponseBuilder.badRequest("Problema di stampa inaspettato");
                } catch (PrintException ex) {
                    return ResponseBuilder.badRequest("Problema di stampa");
                }
            } else {
                return ResponseBuilder.notFound("Stampante principale non trovata");
            }
        }
        return ResponseBuilder.notFound("Tavolo non trovato");
    }
}
