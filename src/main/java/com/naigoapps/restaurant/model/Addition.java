/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST)
    @JoinTable(name = "addition_category",
            joinColumns = {
                @JoinColumn(name = "addition_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "category_id")})
    private List<Category> categories;

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }
    
    

}
