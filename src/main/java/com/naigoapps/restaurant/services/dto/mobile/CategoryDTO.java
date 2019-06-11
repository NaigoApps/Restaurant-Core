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
public class CategoryDTO extends DTO{
    private String name;
    
    private Integer color;
    
    private List<DishDTO> dishes;
    
    private List<AdditionDTO> additions;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }

    public List<DishDTO> getDishes() {
        return dishes;
    }

    public void setAdditions(List<AdditionDTO> additions) {
        this.additions = additions;
    }

    public List<AdditionDTO> getAdditions() {
        return additions;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getColor() {
        return color;
    }

}
