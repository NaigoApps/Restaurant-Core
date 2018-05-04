/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author naigo
 */
@Entity
@DiscriminatorValue("invoice")
public class Invoice extends Bill {

    @ManyToOne
    private Customer customer;
    
    private LocalDateTime printTime;
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    public LocalDateTime getPrintTime() {
        return printTime;
    }
    
    
}
