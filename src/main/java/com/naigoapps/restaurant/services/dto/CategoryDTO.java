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

    private List<DishDTO> dishes;
    
    public CategoryDTO() {
    }

    public CategoryDTO(String uuid, String name, String location, List<DishDTO> dishes) {
        super(uuid);
        this.name = name;
        this.location = location;
        this.dishes = dishes;
    }

    
    
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

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }

    public List<DishDTO> getDishes() {
        return dishes;
    }
    
    

}
