/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "locations")
public class Location extends BaseEntity{
    private String name;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Printer printer;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public Printer getPrinter() {
        return printer;
    }
    
    
}
