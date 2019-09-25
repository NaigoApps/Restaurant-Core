/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;
    
    private String color;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "category")
    private List<Dish> dishes;
    
    @ManyToMany
    @JoinTable(name = "category_addition",
            joinColumns = {
                @JoinColumn(name = "category_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "addition_id")})
    private List<Addition> additions;

    public Category() {
        this.dishes = new ArrayList<>();
        this.additions = new ArrayList<>();
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
        dishes.forEach(dish -> {
            dish.setCategory(this);
        });
    }
    
    public void addDish(Dish d){
        if(!this.dishes.contains(d)){
            this.dishes.add(d);
            d.setCategory(this);
        }
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Addition> getAdditions() {
        return additions;
    }

    public void setAdditions(List<Addition> additions) {
        this.additions = additions;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    
    
}
