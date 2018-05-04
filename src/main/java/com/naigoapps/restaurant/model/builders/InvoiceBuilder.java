/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Invoice;
import com.naigoapps.restaurant.model.Order;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class InvoiceBuilder implements Builder<Invoice>{

    private BillBuilder delegate;
    
    protected LocalDateTime printTime;
    private Customer customer;

    public InvoiceBuilder() {
        this.delegate = new BillBuilder();
    }

    public InvoiceBuilder progressive(int progressive){
        delegate.progressive(progressive);
        return this;
    }
    
    public InvoiceBuilder printTime(LocalDateTime time){
        this.printTime = time;
        return this;
    }
    
    public InvoiceBuilder table(DiningTable table){
        delegate.table(table);
        return this;
    }
    
    public InvoiceBuilder order(Order order){
        delegate.order(order);
        return this;
    }
    
    public InvoiceBuilder orders(List<Order> orders){
        delegate.orders(orders);
        return this;
    }
    
    public InvoiceBuilder total(float total){
        delegate.total(total);
        return this;
    }
    
    public InvoiceBuilder coverCharges(int ccs){
        delegate.coverCharges(ccs);
        return this;
    }
    
    public InvoiceBuilder customer(Customer value){
        this.customer = value;
        return this;
    }
    
    @Override
    public Invoice getContent() {
        Bill bill = delegate.getContent();
        Invoice result = new Invoice();
        result.setPrintTime(printTime);
        result.setCustomer(customer);
        
        result.setProgressive(bill.getProgressive());
        result.setCoverCharges(bill.getCoverCharges());
        result.setOrders(bill.getOrders());
        result.setTable(bill.getTable());
        result.setTotal(bill.getTotal());
        return result;
    }
    
}
