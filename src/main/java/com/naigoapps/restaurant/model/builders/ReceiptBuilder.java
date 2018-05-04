/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Receipt;
import com.naigoapps.restaurant.model.Order;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 * @param <T>
 */
public class ReceiptBuilder implements Builder<Receipt>{

    private BillBuilder delegate;
    
    protected LocalDateTime printTime;
    
    public ReceiptBuilder() {
        delegate = new BillBuilder();
    }
    
    public ReceiptBuilder printTime(LocalDateTime time){
        this.printTime = time;
        return this;
    }
    
    public ReceiptBuilder progressive(int progressive){
        delegate.progressive(progressive);
        return this;
    }
    
    public ReceiptBuilder order(Order order){
        delegate.order(order);
        return this;
    }
    
    public ReceiptBuilder orders(List<Order> orders){
        delegate.orders(orders);
        return this;
    }
    
    public ReceiptBuilder total(float total){
        delegate.total(total);
        return this;
    }
    
    public ReceiptBuilder coverCharges(int ccs){
        delegate.coverCharges(ccs);
        return this;
    }
    
    public ReceiptBuilder table(DiningTable dt){
        delegate.table(dt);
        return this;
    }
    
    @Override
    public Receipt getContent() {
        Bill bill = delegate.getContent();
        Receipt result = new Receipt();
        result.setPrintTime(printTime);
        
        result.setProgressive(bill.getProgressive());
        result.setCoverCharges(bill.getCoverCharges());
        result.setOrders(bill.getOrders());
        result.setTable(bill.getTable());
        result.setTotal(bill.getTotal());
        return result;
    }
    
}
