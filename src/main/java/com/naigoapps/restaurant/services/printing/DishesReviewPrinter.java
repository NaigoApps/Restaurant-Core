/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.utils.mappers.OrdersDishMapper;
import com.naigoapps.restaurant.services.PrinterService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author naigo
 */
public class DishesReviewPrinter implements ObjectPrinter<List<Order>> {

    @Override
    public PrinterService apply(PrinterService ps, List<Order> obj, LocalDateTime time) throws IOException {
        
        Map<Dish, List<Order>> map = obj.stream().collect(OrdersDishMapper.toDishMap());
        for(Map.Entry<Dish, List<Order>> entry : map.entrySet()){
            Dish d = entry.getKey();
            List<Order> orders = entry.getValue();
            ps.printLine(orders.size() + " " + d.getName(),
                    PrinterService.formatPrice(orders.stream()
                            .map(order -> order.getPrice())
                            .reduce(0.0f, (p1, p2) -> p1 + p2)));
        }
        return ps;
    }

}
