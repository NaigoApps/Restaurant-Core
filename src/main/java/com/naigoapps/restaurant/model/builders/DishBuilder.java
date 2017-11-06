/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.DishStatus;

/**
 *
 * @author naigo
 */
public class DishBuilder implements Builder<Dish>{

    private String name;
    private String description;
    private float price;
    private Category category;
    
    public DishBuilder name(String name){
        this.name = name;
        return this;
    }
    
    public DishBuilder description(String description){
        this.description = description;
        return this;
    }
    
    public DishBuilder price(float price){
        this.price = price;
        return this;
    }
    
    public DishBuilder category(Category cat){
        this.category = cat;
        return this;
    }
    
    
    @Override
    public Dish getContent() {
        Dish result = new Dish();
        result.setName(name);
        result.setDescription(description);
        result.setPrice(price);
        result.setStatus(DishStatus.ACTIVE);
        result.setCategory(category);
        return result;
    }
    
}
