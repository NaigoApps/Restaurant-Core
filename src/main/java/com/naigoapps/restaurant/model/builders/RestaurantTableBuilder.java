/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.RestaurantTable;

/**
 *
 * @author naigo
 */
public class RestaurantTableBuilder implements Builder<RestaurantTable>{

    private String name;

    public RestaurantTableBuilder name(String name){
        this.name = name;
        return this;
    }
    
    @Override
    public RestaurantTable getContent() {
        RestaurantTable result = new RestaurantTable();
        result.setName(name);
        return result;
    }
    
}
