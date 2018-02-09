/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.Invoice;

/**
 *
 * @author naigo
 */
public class InvoiceBuilder extends BillBuilder{

    private Customer customer;

    public InvoiceBuilder customer(Customer value){
        this.customer = value;
        return this;
    }
    
    @Override
    public Invoice getContent() {
        Invoice result = new Invoice();
        result.setProgressive(progressive);
        result.setTable(table);
        result.setOrders(orders);
        result.setCustomer(customer);
        return result;
    }
    
}
