/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import java.util.ArrayList;
import java.util.List;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Location;

/**
 *
 * @author naigo
 */
public class CategoryBuilder implements Builder<Category>{

    private String name;
    private String color;
    private Location location;
    private List<Dish> dishes;
    private List<Addition> additions;

    public CategoryBuilder() {
        this.dishes = new ArrayList<>();
        this.additions = new ArrayList<>();
    }
    
    public CategoryBuilder quick(String name, Location location){
        this.name = name;
        this.location = location;
        return this;
    }
    
    public CategoryBuilder name(String name){
        this.name = name;
        return this;
    }
    
    public CategoryBuilder color(String c){
        this.color = c;
        return this;
    }
    
    public CategoryBuilder location(Location value){
        this.location = value;
        return this;
    }
    
    public CategoryBuilder dish(Dish value){
        this.dishes.add(value);
        return this;
    }
    
    public CategoryBuilder addition(Addition value){
        this.additions.add(value);
        return this;
    }
    
    @Override
    public Category getContent() {
        Category result = new Category();
        result.setName(name);
        result.setLocation(location);
        result.setDishes(dishes);
        result.setAdditions(additions);
        result.setColor(color);
        return result;
    }
    
}
