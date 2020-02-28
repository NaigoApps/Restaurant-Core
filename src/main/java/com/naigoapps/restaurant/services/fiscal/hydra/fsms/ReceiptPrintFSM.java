package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.ACK_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.NAK_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.STATUS_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.FAILURE;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.SUCCESS;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.utils.mappers.OrderCategoryMapper;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraGateway;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.CancelReceiptRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.ItemSaleRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.OpenReceiptRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.PayRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiptPrintFSM extends BasicHydraFSM {

    private final Bill bill;

    private int currentItem;

    public ReceiptPrintFSM(HydraGateway gateway,
        Bill bill) {
        super(gateway);
        this.bill = bill;
    }

    @Override
    protected void defineStates(StatesBuilder builder) {
        List<ItemSale> items = new ArrayList<>();
        if (bill.getCoverCharges() > 0) {
            items.add(new ItemSale("COPERTI", bill.getCoverCharges(),
                bill.getTable().getEvening().getCoverCharge()));
        }
        List<Order> orders = bill.getOrders();
        Map<Category, List<Order>> map = orders.stream()
            .collect(OrderCategoryMapper.toCategoryMap());
        for (Map.Entry<Category, List<Order>> entry : map.entrySet()) {
            Category c = entry.getKey();
            double sum = entry.getValue().stream().mapToDouble(Order::getPrice).sum();
            items.add(new ItemSale(c.getName(), 1, sum));
        }

        //@formatter:off
        builder
            .state(ReceiptPrintStateNames.OPEN_ENQ)
                .onEnter(sendEnquireRunnable())
                .transition(ACK_ARRIVED, ReceiptPrintStateNames.OPEN)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(ReceiptPrintStateNames.OPEN)
                .onEnter(sendOpenReceiptRunnable())
                .transition(STATUS_ARRIVED, ReceiptPrintStateNames.PRINT_ENQ)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(ReceiptPrintStateNames.CHECKING)
                .onEnter(() -> {
                    if(currentItem < items.size()){
                        fire(ReceiptPrintEvents.PRINT);
                    }else {
                        fire(ReceiptPrintEvents.PRINTED);
                    }
                })
                .transition(ReceiptPrintEvents.PRINT, ReceiptPrintStateNames.PRINT_ENQ)
                .transition(ReceiptPrintEvents.PRINTED, ReceiptPrintStateNames.PAY_ENQ)
            .end()
            .state(ReceiptPrintStateNames.PRINT_ENQ)
                .onEnter(sendEnquireRunnable())
                .transition(ACK_ARRIVED, ReceiptPrintStateNames.PRINT)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(ReceiptPrintStateNames.PRINT)
                .onEnter(() -> {
                    ItemSale current = items.get(currentItem++);
                    sendPrintRunnable(current).run();
                })
                .transition(STATUS_ARRIVED, ReceiptPrintStateNames.CHECKING)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(ReceiptPrintStateNames.PAY_ENQ)
                .onEnter(sendEnquireRunnable())
                .transition(ACK_ARRIVED, ReceiptPrintStateNames.PAY)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(ReceiptPrintStateNames.PAY)
                .onEnter(sendPayRunnable())
                .transition(STATUS_ARRIVED, SUCCESS)
                .transition(NAK_ARRIVED, FAILURE)
            .end();
        //@formatter:on

    }

    private Runnable sendCancelReceiptRunnable() {
        return sendCommandRunnable(new CancelReceiptRequest());
    }

    private Runnable sendPayRunnable() {
        return sendCommandRunnable(new PayRequest());
    }

    private Runnable sendOpenReceiptRunnable() {
        return sendCommandRunnable(new OpenReceiptRequest());
    }

    private Runnable sendPrintRunnable(ItemSale item) {
        ItemSaleRequest request = new ItemSaleRequest(item.getName(), "", item.getQuantity(),
            item.getPrice(), 1);
        return sendCommandRunnable(request);
    }

    @Override
    protected StateNames firstState() {
        return ReceiptPrintStateNames.OPEN_ENQ;
    }

    private enum ReceiptPrintStateNames implements StateNames {
        OPEN_ENQ, OPEN, CHECKING, PRINT_ENQ, PRINT, PAY_ENQ, PAY
    }

    private enum ReceiptPrintEvents implements Events {
        PRINT, PRINTED
    }

}
