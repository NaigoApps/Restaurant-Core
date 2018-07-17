package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.builders.OrdinationBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.BillDao;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.printing.BillPrinter;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.io.IOException;
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
@Path("/dining-tables")
@Produces(MediaType.APPLICATION_JSON)
public class DiningTableREST {

    private static final String EVENING_NOT_FOUND = "Serata non selezionata";
    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

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

    @Inject
    CustomerDao cDao;

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
                return Response.status(Response.Status.CREATED).entity(DTOAssembler.fromOrdination(ordination)).build();
            }
            return ResponseBuilder.notFound(TABLE_NOT_FOUND);
        }
        return ResponseBuilder.badRequest("tavolo o ordini non validi");
    }

    @PUT
    @Path("{uuid}/orders")
    @Transactional
    public Response editOrders(@PathParam("uuid") String ordinationUuid, OrderDTO[] orders) {
        if (orders != null) {
            Ordination ordination = ordDao.findByUuid(ordinationUuid);
            if (ordination != null) {
                List<Order> oldOrders = new ArrayList<>(ordination.getOrders());
                List<Order> newOrders = buildOrders(orders);

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
                return Response.status(Response.Status.OK).entity(DTOAssembler.fromDiningTable(ordination.getTable())).build();
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

    private List<Order> buildOrders(OrderDTO[] dtos) {
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

    @DELETE
    @Path("{uuid}/ordinations")
    @Transactional
    public Response deleteOrdination(@PathParam("uuid") String tableUuid, String ordUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable table = dtDao.findByUuid(tableUuid);
        Ordination ordination = ordDao.findByUuid(ordUuid);
        if (currentEvening != null && table != null && currentEvening.equals(table.getEvening())) {
            if (ordination != null && table.equals(ordination.getTable())) {
                List<Order> orders = ordination.getOrders();
                if (orders.stream().allMatch(order -> order.getBill() == null)) {
                    ordination.setTable(null);
                    ordination.clearOrders();

                    oDao.getEntityManager().remove(ordination);
                    orders.forEach(order -> oDao.getEntityManager().remove(order));
                    return ResponseBuilder.ok(DTOAssembler.fromDiningTable(table));
                }
                return ResponseBuilder.badRequest("Alcuni ordini hanno conti associati");
            }
            return ResponseBuilder.badRequest("Ordinazione o tavolo non corretti");
        }
        return ResponseBuilder.badRequest("Serata non correttamente selezionata");

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
            Bill bill = bDao.findByUuid(billUuid);
            if (toUpdate != null && bill != null && toUpdate.equals(bill.getTable())) {
                bill.clearOrders();
                bill.setTable(null);
                bDao.getEntityManager().remove(bill);
                if (toUpdate.getBills().isEmpty()) {
                    toUpdate.setStatus(DiningTableStatus.OPEN);
                }
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return ResponseBuilder.notFound("Tavolo o scontrino non trovato");
            }
        } else {
            return ResponseBuilder.notFound(EVENING_NOT_FOUND);
        }
    }

    @PUT
    @Path("{uuid}/bills")
    @Transactional
    public Response addBill(
            @PathParam("uuid") String tableUuid,
            BillDTO bill) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                List<Order> orders = bill.getOrders().stream()
                        .map(uuid -> oDao.findByUuid(uuid))
                        .collect(Collectors.toList());
                if (orders.stream().allMatch(order -> toUpdate.equals(order.getOrdination().getTable()))) {
                    if (orders.stream().allMatch(order -> order.getBill() == null)) {
                        toUpdate.setStatus(DiningTableStatus.CLOSING);
                        return createBill(toUpdate, orders, bill.getCoverCharges(), bill.getTotal());
                    } else {
                        return ResponseBuilder.badRequest("Vi sono ordini già chiusi");
                    }
                } else {
                    return ResponseBuilder.badRequest("Vi sono ordini non appartenenti al tavolo");
                }
            } else {
                return ResponseBuilder.notFound(TABLE_NOT_FOUND);
            }
        } else {
            return ResponseBuilder.notFound(EVENING_NOT_FOUND);
        }
    }

    private Response createBill(DiningTable table, List<Order> orders, int coverCharges, float total) {
        Bill bill = new BillBuilder()
                .progressive(bDao.nextBillProgressive(table.getDate().toLocalDate()))
                .table(table)
                .orders(orders)
                .coverCharges(coverCharges)
                .total(total)
                .getContent();
        dtDao.persist(bill);
        return Response.ok(DTOAssembler.fromDiningTable(table)).build();
    }

    @PUT
    @Path("{t-uuid}/bills/{b-uuid}")
    @Transactional
    public Response updateBill(
            @PathParam("t-uuid") String tableUuid,
            @PathParam("b-uuid") String billUuid,
            BillDTO billDto) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            Bill bill = bDao.findByUuid(billUuid);
            if (toUpdate != null && bill != null && toUpdate.equals(bill.getTable())) {
                bill.clearOrders();
                List<Order> orders = billDto.getOrders().stream()
                        .map(uuid -> oDao.findByUuid(uuid))
                        .collect(Collectors.toList());

                if (orders.stream().allMatch(order -> toUpdate.equals(order.getOrdination().getTable()))) {
                    if (orders.stream().allMatch(order -> order.getBill() == null)) {
                        bill.setOrders(orders);
                        bill.setTotal(billDto.getTotal());
                        bill.setCoverCharges(billDto.getCoverCharges());
                        return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
                    } else {
                        return ResponseBuilder.badRequest("Vi sono ordini già chiusi");
                    }
                } else {
                    return ResponseBuilder.badRequest("Vi sono ordini non appartenenti al tavolo");
                }
            } else {
                return ResponseBuilder.notFound("Tavolo o fattura non trovati");
            }
        } else {
            return ResponseBuilder.notFound(EVENING_NOT_FOUND);
        }
    }

    @POST
    @Path("print-partial-bill")
    @Transactional
    public Response printPartialBill(String billUuid, @QueryParam("generic") Boolean generic) {
        Bill bill = bDao.findByUuid(billUuid);
        if (bill != null) {
            Printer mainPrinter = prDao.findMainPrinter();
            if (mainPrinter != null) {
                try {
                    PrinterService service = new PrinterService(mainPrinter);
                    service.accept(new BillPrinter(Boolean.TRUE.equals(generic)), bill, LocalDateTime.now())
                            .lf(3)
                            .cut()
                            .doPrint();
                    return ResponseBuilder.ok(DTOAssembler.fromDiningTable(bill.getTable()));
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

    @POST
    @Path("bills/{b-uuid}/print-bill")
    @Transactional
    public Response printBill(@PathParam("b-uuid") String billUuid, String customerId, @QueryParam("generic") Boolean generic) {
        Bill bill = bDao.findByUuid(billUuid);
        Customer customer = null;
        if (customerId != null && !customerId.isEmpty()) {
            customer = cDao.findByUuid(customerId);
        }
        if (bill != null && (customer != null || customerId == null || customerId.isEmpty())) {
            Printer fiscalPrinter = prDao.findFiscalPrinter();
            if (fiscalPrinter != null) {
                try {
                    bill.getTable().setStatus(DiningTableStatus.CLOSING);
                    if (customer != null) {
                        bill.setCustomer(customer);
                        bill.setProgressive(bDao.nextInvoiceProgressive(bill.getTable().getDate().toLocalDate()));
                    } else {
                        bill.setProgressive(bDao.nextReceiptProgressive(bill.getTable().getDate().toLocalDate()));
                    }
                    bill.setPrintTime(LocalDateTime.now());

                    PrinterService service = new PrinterService(fiscalPrinter);
                    service.accept(new BillPrinter(Boolean.TRUE.equals(generic), customer), bill, LocalDateTime.now())
                            .lf(3)
                            .cut()
                            .doPrint();
                    
                    boolean closed = bill.getTable().getOrdinations().stream()
                            .allMatch(ordination -> {
                                return ordination.getOrders().stream()
                                        .noneMatch(order -> order.getBill() == null || order.getBill().getPrintDate() == null);
                            });
                    if(closed){
                        bill.getTable().setStatus(DiningTableStatus.CLOSED);
                    }
                    
                    return ResponseBuilder.ok(DTOAssembler.fromDiningTable(bill.getTable()));
                } catch (IOException ex) {
                    return ResponseBuilder.badRequest("Problema di stampa inaspettato");
                } catch (PrintException ex) {
                    return ResponseBuilder.badRequest("Problema di stampa");
                }
            } else {
                return ResponseBuilder.notFound("Stampante fiscale non trovata");
            }
        }
        return ResponseBuilder.notFound("Conto o cliente non trovato");
    }

    @POST
    @Path("{uuid}/lock")
    @Transactional
    public Response lockTable(@PathParam("uuid") String tableUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                boolean ordersOk = toUpdate.listOrders().stream().allMatch(order -> order.getBill() != null);
                if (ordersOk) {
                    toUpdate.setStatus(DiningTableStatus.CLOSED);
                    return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
                }
                return ResponseBuilder.badRequest("Il tavolo contiene ordini non chiusi");
            } else {
                return ResponseBuilder.notFound("Tavolo non trovato");
            }
        } else {
            return ResponseBuilder.badRequest(EVENING_NOT_FOUND);
        }
    }
}
