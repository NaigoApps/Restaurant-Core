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

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Dish> dishes;

    public Category() {
    }

    public Category(Category c) {
        this.name = c.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void addDish(Dish d) {
        if (!this.dishes.contains(d)) {
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

}
