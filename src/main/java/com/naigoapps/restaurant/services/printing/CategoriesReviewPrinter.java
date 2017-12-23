/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.utils.mappers.OrderCategoryMapper;
import com.naigoapps.restaurant.services.PrinterService;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author naigo
 */
public class CategoriesReviewPrinter implements ObjectPrinter<List<Order>> {

    @Override
    public PrinterService apply(PrinterService ps, List<Order> obj) throws IOException {
        
        Map<Category, List<Order>> map = obj.stream().collect(OrderCategoryMapper.toCategoryMap());
        for(Map.Entry<Category, List<Order>> entry : map.entrySet()){
            Category c = entry.getKey();
            List<Order> orders = entry.getValue();
            ps.printLine(orders.size() + " " + c.getName(),
                    PrinterService.formatPrice(orders.stream()
                            .map(order -> order.getPrice())
                            .reduce(0.0f, (p1, p2) -> p1 + p2)));
        }
        return ps;
    }

}
