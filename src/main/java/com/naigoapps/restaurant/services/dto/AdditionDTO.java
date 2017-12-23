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
public class AdditionDTO extends DTO{

    private String name;
    private float price;

    private boolean generic;

    public AdditionDTO() {
    }

    public AdditionDTO(String uuid, String name, float price, boolean generic) {
        super(uuid);
        this.name = name;
        this.price = price;
        this.generic = generic;
    }

    
    
    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    public boolean isGeneric() {
        return generic;
    }
    
    

}
