/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.DishStatus;
import java.util.ArrayList;

/**
 *
 * @author naigo
 */
public class CategoryBuilder implements Builder<Category>{

    private String name;
    
    public CategoryBuilder name(String name){
        this.name = name;
        return this;
    }
    
    @Override
    public Category getContent() {
        Category result = new Category();
        result.setName(name);
        result.setDishes(new ArrayList<>());
        return result;
    }
    
}
