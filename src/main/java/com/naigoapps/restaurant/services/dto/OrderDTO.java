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
public class OrderDTO extends DTO{

    private final String dish;
    
    private final List<String> additions;
    
    private final float price;
    
    private final String notes;
    
    private final String phase;
    
    private final String bill;

    public OrderDTO() {
        this.dish = null;
        this.additions = null;
        this.price = 0;
        this.notes = null;
        this.phase = null;
        this.bill = null;
    }


    public OrderDTO(String dish, List<String> additions, float price, String notes, String phase, String bill, String uuid) {
        super(uuid);
        this.dish = dish;
        this.additions = additions;
        this.price = price;
        this.notes = notes;
        this.phase = phase;
        this.bill = bill;
    }

    public String getDish() {
        return dish;
    }

    public List<String> getAdditions() {
        return Collections.unmodifiableList(additions);
    }

    public float getPrice() {
        return price;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhase() {
        return phase;
    }

    public String getBill() {
        return bill;
    }

   
    
}
