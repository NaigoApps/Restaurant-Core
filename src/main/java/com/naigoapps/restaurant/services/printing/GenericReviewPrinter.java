/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.services.printing.services.PrintingService;

/**
 *
 * @author naigo
 */
public class GenericReviewPrinter implements ObjectPrinter<Bill> {

    @Override
    public PrintingService apply(PrintingService ps, Bill bill, LocalDateTime time) throws IOException {
    	List<Order> orders = bill.getOrders();

    	return ps.printLine("GENERICO", PrintingService.formatPrice(
        		bill.getTable().getEvening().getCoverCharge() * bill.getCoverCharges() + 
                orders.stream()
                        .map(Order::getPrice)
                        .reduce(0.0f, (p1, p2) -> p1 + p2)));
    }

}
