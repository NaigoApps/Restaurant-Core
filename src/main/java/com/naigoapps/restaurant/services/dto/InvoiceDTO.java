/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author naigo
 */
public class InvoiceDTO extends BillDTO{

    private final String customer;

    public InvoiceDTO() {
        this.customer = null;
    }


    public InvoiceDTO(String customer, String uuid, int progressive, String table, List<String> orders) {
        super(uuid, progressive, table, orders);
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }
    
    

}
