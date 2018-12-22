package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.dao.BillDao;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.printing.BillPrinter;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.io.IOException;
import java.time.LocalDate;
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
@Path("/bills")
@Produces(MediaType.APPLICATION_JSON)
public class BillsREST {

    private static final String EVENING_NOT_FOUND = "Serata non selezionata";
    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

    @Inject
    private EveningManager eveningManager;

    @Inject
    private DiningTableDao dtDao;

    @Inject
    private OrderDao oDao;

    @Inject
    private PrinterDao prDao;

    @Inject
    private BillDao bDao;

    @Inject
    private CustomerDao cDao;

    @GET
    @Transactional
    public Response getBills() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            List<BillDTO> bills = new ArrayList<>();
            e.getDiningTables()
                    .forEach(dt -> bills.addAll(
                    dt.getBills().stream()
                            .map(DTOAssembler::fromBill)
                            .collect(Collectors.toList())));
            return Response.status(Response.Status.OK).entity(bills).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("{tableUuid}")
    @Transactional
    public Response getBills(@PathParam("tableUuid") String tableUuid) {
        Evening e = eveningManager.getSelectedEvening();
        DiningTable table = dtDao.findByUuid(tableUuid);
        if (e != null) {
            List<BillDTO> bills = table.getBills().stream()
                    .map(DTOAssembler::fromBill)
                    .collect(Collectors.toList());
            return Response.status(Response.Status.OK).entity(bills).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("{uuid}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response removeBill(@PathParam("uuid") String billUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            Bill bill = bDao.findByUuid(billUuid);
            if (bill != null) {
                DiningTable table = bill.getTable();
                if (bill.getTable().getEvening().equals(currentEvening)) {
                    bill.clearOrders();
                    bill.setTable(null);
                    bDao.getEntityManager().remove(bill);
                    table.updateStatus();
                    return Response.ok(bill.getUuid()).build();
                } else {
                    return ResponseBuilder.notFound("Serata non valida");
                }
            } else {
                return ResponseBuilder.notFound("Scontrino non trovato");
            }
        } else {
            return ResponseBuilder.notFound(EVENING_NOT_FOUND);
        }
    }

    @POST
    @Transactional
    public Response addBill(
            @QueryParam("table") String tableUuid,
            BillDTO bill) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
                List<Order> orders = bill.getOrders().stream()
                        .map(uuid -> oDao.findByUuid(uuid))
                        .collect(Collectors.toList());
                if (orders.stream().allMatch(order -> toUpdate.equals(order.getOrdination().getTable()))) {
                    if (orders.stream().allMatch(order -> order.getBill() == null)) {
                        Bill b = createBill(toUpdate, orders, bill.getCoverCharges(), bill.getTotal());
                        toUpdate.updateStatus();
                        return ResponseBuilder.ok(DTOAssembler.fromBill(b));
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

    @POST
    @Path("quick")
    @Transactional
    public Response quickBill(
            @QueryParam("table") String tableUuid) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
                List<Order> orders = toUpdate.listOrders().stream()
                        .filter(order -> order.getBill() == null)
                        .collect(Collectors.toList());
                if (!orders.isEmpty()) {
                    if (orders.stream().allMatch(order -> order.getPrice() != 0)) {
                        int doneCcs = toUpdate.getBills().stream()
                                .collect(Collectors.summingInt(Bill::getCoverCharges));
                        int coverCharges = toUpdate.getCoverCharges() - doneCcs;
                        float total = orders.stream()
                                .collect(Collectors.summingDouble(Order::getPrice))
                                .floatValue();
                        total += coverCharges * currentEvening.getCoverCharge();
                        Bill b = createBill(toUpdate, orders, coverCharges, total);
                        toUpdate.updateStatus();
                        return ResponseBuilder.ok(DTOAssembler.fromBill(b));
                    } else {
                        return ResponseBuilder.badRequest("Assegnare un prezzo a tutti gli ordini");
                    }
                } else {
                    return ResponseBuilder.badRequest("Nessun ordine disponibile");
                }
            } else {
                return ResponseBuilder.notFound(TABLE_NOT_FOUND);
            }
        } else {
            return ResponseBuilder.notFound(EVENING_NOT_FOUND);
        }
    }

    private Bill createBill(DiningTable table, List<Order> orders, int coverCharges, float total) {
        Bill bill = new BillBuilder()
                .progressive(bDao.nextBillProgressive(table.getEvening()))
                .table(table)
                .orders(orders)
                .coverCharges(coverCharges)
                .total(total)
                .getContent();
        dtDao.persist(bill);
        return bill;
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
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            Bill bill = bDao.findByUuid(billUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening) && bill != null && toUpdate.equals(bill.getTable())) {
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
    @Path("{uuid}/soft-print")
    @Transactional
    public Response printSoftBill(@PathParam("uuid") String billUuid, @QueryParam("generic") Boolean generic) {
        Bill bill = bDao.findByUuid(billUuid);
        if (bill != null) {
            Printer mainPrinter = prDao.findMainPrinter();
            if (mainPrinter != null) {
                try {
                    PrintingService service = PrintingServiceProvider.get(mainPrinter);
                    service.accept(new BillPrinter(Boolean.TRUE.equals(generic)), bill, LocalDateTime.now())
                            .lf(3)
                            .cut()
                            .doPrint();
                    return ResponseBuilder.ok(DTOAssembler.fromBill(bill));
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
    @Path("{uuid}/print")
    @Transactional
    public Response printBill(@PathParam("uuid") String billUuid, String customerId, @QueryParam("generic") Boolean generic) {
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
                        bill.setProgressive(bDao.nextInvoiceProgressive(LocalDate.now()));
                    } else {
                        bill.setProgressive(bDao.nextReceiptProgressive(LocalDate.now()));
                    }
                    bill.setPrintTime(LocalDateTime.now());

                    PrintingService service = PrintingServiceProvider.get(fiscalPrinter);
                    service.accept(new BillPrinter(Boolean.TRUE.equals(generic), customer), bill, LocalDateTime.now())
                            .lf(3)
                            .cut()
                            .doPrint();

                    bill.getTable().updateStatus();

                    return ResponseBuilder.ok(DTOAssembler.fromBill(bill));
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

}
