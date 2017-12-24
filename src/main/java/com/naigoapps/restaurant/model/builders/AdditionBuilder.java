/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Addition;

/**
 *
 * @author naigo
 */
public class AdditionBuilder implements Builder<Addition>{

    private String name;
    private float price;
    private boolean generic;
    
    public AdditionBuilder name(String name){
        this.name = name;
        return this;
    }
    
    public AdditionBuilder price(float price){
        this.price = price;
        return this;
    }
    
    public AdditionBuilder generic(boolean generic){
        this.generic = generic;
        return this;
    }
    
    @Override
    public Addition getContent() {
        Addition addition = new Addition();
        addition.setName(name);
        addition.setPrice(price);
        addition.setGeneric(generic);
        return addition;
    }
    
}
