/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile;

import java.util.List;

/**
 *
 * @author naigo
 */
public class OrdersGroupDTO extends DTO {

    private DishDTO dish;

    private List<AdditionDTO> additions;

    private int quantity;

    private float price;

    private String notes;

    public OrdersGroupDTO() {
    }

    public DishDTO getDish() {
        return dish;
    }

    public void setDish(DishDTO dish) {
        this.dish = dish;
    }

    public List<AdditionDTO> getAdditions() {
        return additions;
    }

    public void setAdditions(List<AdditionDTO> additions) {
        this.additions = additions;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
