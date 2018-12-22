/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class GenericReviewPrinter implements ObjectPrinter<List<Order>> {

    @Override
    public PrintingService apply(PrintingService ps, List<Order> orders, LocalDateTime time) throws IOException {
        return ps.printLine("GENERICO", PrintingService.formatPrice(
                orders.stream()
                        .map(order -> order.getPrice())
                        .reduce(0.0f, (p1, p2) -> p1 + p2)));
    }

}
