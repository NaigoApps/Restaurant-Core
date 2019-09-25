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
    
    private String categoryId;
    
    private String name;
    
    private float price;
    
    private String description;
    
    private DishStatus status;

    private String color;
    
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public DishStatus getStatus() {
        return status;
    }

    public void setStatus(DishStatus status) {
        this.status = status;
    }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
    
}
