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
public class CategoryDTO extends DTO{
    private String name;
    
    private String location;

    private Integer color;
    
    private List<String> dishes;
    
    private List<String> additions;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setAdditions(List<String> additions) {
        this.additions = additions;
    }

    public List<String> getAdditions() {
        return additions;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getColor() {
        return color;
    }

}
