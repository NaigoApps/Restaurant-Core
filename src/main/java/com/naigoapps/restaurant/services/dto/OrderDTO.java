/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.util.List;

/**
 *
 * @author naigo
 */
public class OrderDTO extends DTO{

    private String dish;
    
    private String ordination;
    
    private List<String> additions;
    
    private float price;
    
    private String notes;
    
    private String phase;

    public OrderDTO() {
    }

    public OrderDTO(String uuid, String ordination, String dish, List<String> additions, float price, String notes, String phase) {
        super(uuid);
        this.dish = dish;
        this.ordination = ordination;
        this.additions = additions;
        this.price = price;
        this.notes = notes;
        this.phase = phase;
    }

    public String getOrdination() {
        return ordination;
    }

    public void setOrdination(String ordination) {
        this.ordination = ordination;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getAdditions() {
        return additions;
    }

    public void setAdditions(List<String> additions) {
        this.additions = additions;
    }

    
}
