/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import com.naigoapps.restaurant.model.DishStatus;

/**
 *
 * @author naigo
 */
public class DishDTO extends DTO{
    
    private String category;
    
    private String name;
    
    private float price;
    
    private String description;
    
    private DishStatus status;

    public DishDTO() {
    }

    public DishDTO(String uuid, String categoryUuid, String name, float price, String description, DishStatus status) {
        super(uuid);
        this.category = categoryUuid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
    }

    
    
    public DishStatus getStatus() {
        return status;
    }

    public void setStatus(DishStatus status) {
        this.status = status;
    }    
    
    public String getCategory() {
        return category;
    }
    
    
    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
