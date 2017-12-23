/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "additions")
public class Addition extends BaseEntity {

    private String name;
    private float price;

    private boolean generic;
    
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

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

}
