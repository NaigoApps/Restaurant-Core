/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Order;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 */
public class BillBuilder implements Builder<Bill>{

    protected DiningTable table;
    protected List<Order> orders;

    public BillBuilder() {
        orders = new ArrayList<>();
    }
    
    public BillBuilder table(DiningTable table){
        this.table = table;
        return this;
    }
    
    public BillBuilder order(Order order){
        this.orders.add(order);
        return this;
    }
    
    public BillBuilder orders(List<Order> orders){
        this.orders.addAll(orders);
        return this;
    }
    
    @Override
    public Bill getContent() {
        Bill result = new Bill();
        result.setTable(table);
        result.setOrders(orders);
        return result;
    }
    
}
