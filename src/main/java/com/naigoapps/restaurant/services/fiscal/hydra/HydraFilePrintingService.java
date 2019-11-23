/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.fiscal.hydra;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.utils.mappers.OrderCategoryMapper;

/**
 *
 * @author naigo
 */
public class HydraFilePrintingService {

	public void print(Bill bill) throws IOException {
		HydraCommandsBuilder builder = new HydraCommandsBuilder();
		
    	List<Order> obj = bill.getOrders();
    	
    	builder.openReceipt();

		if (bill.getCoverCharges() > 0) {
			builder.itemSale("COPERTI", "", bill.getCoverCharges(), bill.getTable().getEvening().getCoverCharge(), 1);
		}
    	
        Map<Category, List<Order>> map = obj.stream().collect(OrderCategoryMapper.toCategoryMap());
        for(Map.Entry<Category, List<Order>> entry : map.entrySet()){
            Category c = entry.getKey();
            double sum = entry.getValue().stream().mapToDouble(Order::getPrice).sum();
            builder.itemSale(c.getName(), "", 1, sum, 1);
        }
        
        builder.pay();
        
        FileOutputStream fos = new FileOutputStream(bill.getUuid() + ".txt");
        fos.write(builder.toString().getBytes(StandardCharsets.US_ASCII));
        fos.close();
	}
	
	

}
