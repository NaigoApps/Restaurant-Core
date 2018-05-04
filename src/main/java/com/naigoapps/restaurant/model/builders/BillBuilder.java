/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Receipt;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Order;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 * @param <T>
 */
public class BillBuilder implements Builder<Bill>{

    protected DiningTable table;
    protected List<Order> orders;
    protected int coverCharges;
    protected float total;
    protected Integer progressive;

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
    
    public BillBuilder total(float total){
        this.total = total;
        return this;
    }
    
    public BillBuilder coverCharges(int ccs){
        this.coverCharges = ccs;
        return this;
    }
    
    public BillBuilder progressive(Integer prog){
        this.progressive = prog;
        return this;
    }
    
    @Override
    public Bill getContent() {
        Bill result = new Bill();
        result.setTable(table);
        result.setOrders(orders);
        result.setTotal(total);
        result.setCoverCharges(coverCharges);
        result.setProgressive(progressive);
        return result;
    }
    
}
