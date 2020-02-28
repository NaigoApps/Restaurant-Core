package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.*;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.dao.*;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.mappers.BillMapper;
import com.naigoapps.restaurant.services.dto.mappers.OrdinationMapper;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraNetworkPrintingService;
import com.naigoapps.restaurant.services.printing.BillPrinter;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.print.PrintException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author naigo
 */
@RequestMapping("/rest/bills")
@RestController
@Transactional
public class BillsREST {

    private static final String EVENING_NOT_FOUND = "Serata non selezionata";
    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

    @Autowired
    private EveningManager eveningManager;

    @Autowired
    private DiningTableDao dtDao;

    @Autowired
    private OrderDao oDao;

    @Autowired
    private PrinterDao prDao;

    @Autowired
    private BillDao dao;

    @Autowired
    private CustomerDao cDao;

    @Autowired
    private BillMapper mapper;

    @Autowired
    private OrdinationMapper oMapper;

    @Autowired
    private HydraNetworkPrintingService printingService;

    @GetMapping
    public List<BillDTO> getBills() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            List<BillDTO> bills = new ArrayList<>();
            e.getDiningTables()
                    .forEach(dt -> bills.addAll(dt.getBills().stream().map(mapper::map).collect(Collectors.toList())));
            return bills;
        } else {
            throw new RuntimeException(EVENING_NOT_FOUND);
        }
    }

    @GetMapping("{billUuid}")
    public BillDTO getBill(@PathVariable("billUuid") String billUuid) {
        return mapper.map(dao.findByUuid(billUuid));
    }

    @GetMapping("remaining/{tableUuid}")
    public DiningTableDTO getRemaining(@PathVariable("tableUuid") String tableUuid) {
        DiningTable table = dtDao.findByUuid(tableUuid);

        int closedCC = table.getBills().stream().mapToInt(Bill::getCoverCharges).sum();

        DiningTableDTO result = new DiningTableDTO();
        result.setCoverCharge(table.getEvening().getCoverCharge());
        result.setCoverCharges(table.getCoverCharges() - closedCC);
        result.setOrders(oMapper
                .group(table.getOrders().stream().filter(o -> o.getBill() == null).collect(Collectors.toList())));
        result.setTotal(result.getCoverCharges() * result.getCoverCharge()
                + result.getOrders().stream().mapToDouble(o -> o.getPrice() * o.getQuantity()).sum());

        return result;
    }

    @PostMapping
    public String addBill(@RequestParam("table") String tableUuid, @RequestBody CreateBillParams params) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null) {
                List<Order> orders = params.getOrders().stream()
                        .map(oDao::findByUuid)
                        .filter(o -> o.getBill() == null)
                        .collect(Collectors.toList());

                if (toUpdate.remainingCoverCharges() >= params.getCcs() && !orders.isEmpty()) {
                    Bill bill = new Bill();
                    bill.setProgressive(dao.nextBillProgressive(currentEvening));
                    bill.setTable(toUpdate);
                    bill.setCoverCharges(params.getCcs());
                    bill.setOrders(orders);
                    bill.setTotal(params.getTotal());
                    dao.persist(bill);
                    toUpdate.updateStatus();
                    return bill.getUuid();
                } else {
                    throw new RuntimeException("Ordini non validi presenti nel conto");
                }

            } else {
                throw new RuntimeException(TABLE_NOT_FOUND);
            }
        } else {
            throw new RuntimeException(EVENING_NOT_FOUND);
        }
    }

    @PostMapping("quick")
    public BillDTO quickBill(@RequestParam("table") String tableUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
                List<Order> orders = toUpdate.getOrders().stream().filter(order -> order.getBill() == null)
                        .collect(Collectors.toList());
                if (!orders.isEmpty()) {
                    if (orders.stream().allMatch(order -> order.getPrice() != 0)) {
                        int doneCcs = toUpdate.getBills().stream()
                                .collect(Collectors.summingInt(Bill::getCoverCharges));
                        int coverCharges = toUpdate.getCoverCharges() - doneCcs;
                        float total = orders.stream().collect(Collectors.summingDouble(Order::getPrice)).floatValue();
                        total += coverCharges * currentEvening.getCoverCharge();
                        Bill b = createBill(toUpdate, orders, coverCharges, total);
                        toUpdate.updateStatus();
                        return mapper.map(b);
                    } else {
                        throw new RuntimeException("Assegnare un prezzo a tutti gli ordini");
                    }
                } else {
                    throw new RuntimeException("Nessun ordine disponibile");
                }
            } else {
                throw new RuntimeException(TABLE_NOT_FOUND);
            }
        } else {
            throw new RuntimeException(EVENING_NOT_FOUND);
        }
    }

    private Bill createBill(DiningTable table, List<Order> orders, int coverCharges, float total) {
        Bill bill = new BillBuilder().progressive(dao.nextBillProgressive(table.getEvening())).table(table)
                .orders(orders).coverCharges(coverCharges).total(total).getContent();
        dtDao.persist(bill);
        return bill;
    }

    @PostMapping("{uuid}/soft-print")
    public void printSoftBill(@PathVariable("uuid") String billUuid,
                              @RequestParam(required = false, value = "generic") Boolean generic) {
        Bill bill = dao.findByUuid(billUuid);
        if (bill != null) {
            Printer mainPrinter = prDao.findMainPrinter();
            if (mainPrinter != null) {
                try {
                    PrintingService service = PrintingServiceProvider.get(mainPrinter);
                    service.accept(new BillPrinter(Boolean.TRUE.equals(generic)), bill, LocalDateTime.now()).lf(3).cut()
                            .doPrint();
                    mapper.map(bill);
                } catch (IOException ex) {
                    throw new RuntimeException("Problema di stampa inaspettato");
                } catch (PrintException ex) {
                    throw new RuntimeException("Problema di stampa");
                }
            } else {
                throw new IllegalStateException("Stampante principale non trovata");
            }
        } else {
            throw new RuntimeException("Conto non trovato");
        }
    }

    @PostMapping("{uuid}/print")
    public void printBill(@PathVariable("uuid") String billUuid, @RequestBody(required = false) String customerId,
                          @RequestParam(required = false, value = "generic") Boolean generic) {
        Bill bill = dao.findByUuid(billUuid);
        Customer customer = null;
        if (customerId != null && !customerId.isEmpty()) {
            customer = cDao.findByUuid(customerId);
        }
        if (bill != null && (customer != null || customerId == null || customerId.isEmpty())) {
            Printer fiscalPrinter = prDao.findMainPrinter();
            if (fiscalPrinter != null) {
                try {
                    bill.getTable().setStatus(DiningTableStatus.CLOSING);
                    if (customer != null) {
                        bill.setCustomer(customer);
                        bill.setProgressive(dao.nextInvoiceProgressive(LocalDate.now()));
                    } else {
                        bill.setProgressive(dao.nextReceiptProgressive(LocalDate.now()));
                    }
                    bill.setPrintTime(LocalDateTime.now());

                    printingService.print(bill);

                    bill.getTable().updateStatus();

                } catch (IOException ex) {
                    throw new RuntimeException("Problema di stampa inaspettato", ex);
                }
            } else {
                throw new RuntimeException("Stampante fiscale non trovata");
            }
        } else {
            throw new RuntimeException("Conto o cliente non trovato");
        }
    }

    @DeleteMapping("{uuid}")
    public void removeBill(@PathVariable("uuid") String billUuid) {
        Bill bill = dao.findByUuid(billUuid);
        if (bill != null) {
            DiningTable table = bill.getTable();
            bill.clearOrders();
            bill.setTable(null);
            dao.getEntityManager().remove(bill);
            table.updateStatus();
        } else {
            throw new RuntimeException("Conto non trovato");
        }
    }

    @DeleteMapping("{uuid}/deep")
    public void removeBillAndOrders(@PathVariable("uuid") String billUuid) {
        Bill bill = dao.findByUuid(billUuid);
        if (bill != null) {
            Set<Order> ordersToRemove = new HashSet<>(bill.getOrders());
            Set<Ordination> ordinationsToCheck = ordersToRemove.stream().map(Order::getOrdination)
                    .collect(Collectors.toSet());

            ordersToRemove.forEach(order -> {
                order.setOrdination(null);
                dao.delete(order);
            });

            ordinationsToCheck.stream().filter(ordination -> ordination.getOrders().isEmpty()).forEach(ordination -> {
                ordination.setTable(null);
                dao.delete(ordination);
            });

            DiningTable table = bill.getTable();
            table.setCoverCharges(table.getCoverCharges() - bill.getCoverCharges());
            bill.setTable(null);
            bill.clearOrders();
            dao.delete(bill);
            table.updateStatus();
        } else {
            throw new RuntimeException("Conto non trovato");
        }
    }

}
