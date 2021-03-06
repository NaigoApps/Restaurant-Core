/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.utils.mappers.OrderCategoryMapper;
import com.naigoapps.restaurant.services.printing.services.PrintingService;

/**
 *
 * @author naigo
 */
public class CategoriesReviewPrinter implements ObjectPrinter<Bill> {

    @Override
    public PrintingService apply(PrintingService ps, Bill bill, LocalDateTime time) throws IOException {
        
    	List<Order> obj = bill.getOrders();

		if (bill.getCoverCharges() > 0) {
			float ccs = bill.getTable().getEvening().getCoverCharge() * bill.getCoverCharges();
			ps.printLine(bill.getCoverCharges() + " COPERTI", PrintingService.formatPrice(ccs));
		}
    	
        Map<Category, List<Order>> map = obj.stream().collect(OrderCategoryMapper.toCategoryMap());
        for(Map.Entry<Category, List<Order>> entry : map.entrySet()){
            Category c = entry.getKey();
            List<Order> orders = entry.getValue();
            ps.printLine(orders.size() + " " + c.getName(),
                    PrintingService.formatPrice(orders.stream().mapToDouble(Order::getPrice).sum()));
        }
        return ps;
    }

}
