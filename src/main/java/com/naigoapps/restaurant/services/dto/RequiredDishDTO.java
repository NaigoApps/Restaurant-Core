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
public class RequiredDishDTO extends DTO{

    private String dish;
    
    private int quantity;
    
    private String ordination;
    
    private String table;
    
    private List<AdditionDTO> additions;
    
    private float price;
    
    private String notes;
    
    private String phase;

    public RequiredDishDTO() {
    }

    public RequiredDishDTO(String uuid, String ordination, String table, int quantity, String dish, List<AdditionDTO> additions, float price, String notes, String phase) {
        super(uuid);
        this.dish = dish;
        this.table = table;
        this.ordination = ordination;
        this.quantity = quantity;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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

    public List<AdditionDTO> getAdditions() {
        return additions;
    }

    public void setAdditions(List<AdditionDTO> additions) {
        this.additions = additions;
    }

    
}
