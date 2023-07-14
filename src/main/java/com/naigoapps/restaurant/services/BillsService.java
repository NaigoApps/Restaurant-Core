package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.*;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.dao.*;
import com.naigoapps.restaurant.services.printing.BillPrinter;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import com.naigoapps.restaurant.services.rs.CreateBillParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.PrintException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillsService {

    private static final String EVENING_NOT_FOUND = "Serata non selezionata";
    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

    @Autowired
    private DiningTableDao dtDao;

    @Autowired
    private OrderDao oDao;

    @Autowired
    private PrinterDao prDao;

    @Autowired
    private BillDao bDao;

    @Autowired
    private SettingsDao sDao;

    @Autowired
    private EveningManager eveningManager;

    public List<Bill> findEveningBills() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            List<Bill> bills = new ArrayList<>();
            e.getDiningTables().forEach(dt -> bills.addAll(dt.getBills()));
            return bills;
        } else {
            throw new RuntimeException(EVENING_NOT_FOUND);
        }
    }

    public Bill findBill(String billUuid) {
        return bDao.findByUuid(billUuid);
    }

    public int getCoverChargesToPay(String tableUuid) {
        DiningTable table = dtDao.findByUuid(tableUuid);
        int closedCC = table.getBills().stream().mapToInt(Bill::getCoverCharges).sum();
        return table.getCoverCharges() - closedCC;
    }

    public List<Order> getOrdersToPay(String tableUuid) {
        DiningTable table = dtDao.findByUuid(tableUuid);
        return table.getOrders().stream().filter(o -> o.getBill() == null).collect(Collectors.toList());
    }

    public float getTableCoverCharge(String tableUuid) {
        return dtDao.findByUuid(tableUuid).getEvening().getCoverCharge();
    }

    public String addBill(String tableUuid, CreateBillParams params) {
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
                    bill.setProgressive(bDao.nextBillProgressive(currentEvening));
                    bill.setTable(toUpdate);
                    bill.setCoverCharges(params.getCcs());
                    bill.setOrders(orders);
                    bill.setTotal(params.getTotal());
                    bDao.persist(bill);
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

    public Bill quickBill(String tableUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
                List<Order> orders = toUpdate.getOrders().stream().filter(order -> order.getBill() == null)
                        .collect(Collectors.toList());
                if (!orders.isEmpty()) {
                    if (orders.stream().allMatch(order -> order.getPrice() != 0)) {
                        int doneCcs = toUpdate.getBills().stream().mapToInt(Bill::getCoverCharges).sum();
                        int coverCharges = toUpdate.getCoverCharges() - doneCcs;
                        float total = ((Double) orders.stream().mapToDouble(Order::getPrice).sum()).floatValue();
                        total += coverCharges * currentEvening.getCoverCharge();
                        Bill b = createBill(toUpdate, orders, coverCharges, total);
                        toUpdate.updateStatus();
                        return b;
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
        Bill bill = new BillBuilder().progressive(bDao.nextBillProgressive(table.getEvening())).table(table)
                .orders(orders).coverCharges(coverCharges).total(total).getContent();
        dtDao.persist(bill);
        return bill;
    }

    public void partialBill(String tableUuid, Integer parts, Float price) {
        DiningTable table = dtDao.findByUuid(tableUuid);
        for (int i = 0; i < parts; i++) {
            Order fixedOrder = createFixedOrder(price);
            Bill bill = new BillBuilder()
                    .progressive(bDao.nextBillProgressive(table.getEvening()))
                    .table(table)
                    .orders(Collections.singletonList(fixedOrder))
                    .coverCharges(0)
                    .total(price).getContent();
            dtDao.persist(fixedOrder);
            dtDao.persist(bill);
        }
    }

    private Order createFixedOrder(Float price) {
        return new OrderBuilder()
                .notes("PASTO A PREZZO FISSO")
                .price(price)
                .getContent();
    }

    public void printSoftBill(String billUuid, Boolean generic) {
        Bill bill = bDao.findByUuid(billUuid);
        if (bill != null) {
            Printer mainPrinter = prDao.findMainPrinter();
            if (mainPrinter != null) {
                try {
                    PrintingService service = PrintingServiceProvider.get(mainPrinter);
                    service.accept(new BillPrinter(Boolean.TRUE.equals(generic), sDao.find().getBillHeader()),
                                    bill,
                                    LocalDateTime.now())
                            .lf(3)
                            .cut()
                            .doPrint();
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

    public void removeBill(String billUuid) {
        Bill bill = bDao.findByUuid(billUuid);
        if (bill != null) {
            DiningTable table = bill.getTable();
            bill.clearOrders();
            bill.setTable(null);
            bDao.getEntityManager().remove(bill);
            table.updateStatus();
        } else {
            throw new RuntimeException("Conto non trovato");
        }
    }

    public void removeBillAndOrders(String billUuid) {
        Bill bill = bDao.findByUuid(billUuid);
        if (bill != null) {
            Set<Order> ordersToRemove = new HashSet<>(bill.getOrders());
            Set<Ordination> ordinationsToCheck = ordersToRemove.stream().map(Order::getOrdination)
                    .collect(Collectors.toSet());

            ordersToRemove.forEach(order -> {
                order.setOrdination(null);
                bDao.delete(order);
            });

            ordinationsToCheck.stream()
                    .filter(Objects::nonNull)
                    .filter(ordination -> ordination.getOrders().isEmpty()).forEach(ordination -> {
                        ordination.setTable(null);
                        bDao.delete(ordination);
                    });

            DiningTable table = bill.getTable();
            table.setCoverCharges(table.getCoverCharges() - bill.getCoverCharges());
            bill.setTable(null);
            bill.clearOrders();
            bDao.delete(bill);
            table.updateStatus();
        } else {
            throw new RuntimeException("Conto non trovato");
        }
    }
}
