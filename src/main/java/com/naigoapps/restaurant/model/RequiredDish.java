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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "required_dishes")
public class RequiredDish extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Ordination ordination;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Dish dish;
    
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST)
    @JoinTable(name = "order_addition",
            joinColumns = {
                @JoinColumn(name = "order_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "addition_id")})
    private List<Addition> additions;
    
    private float price;
    
    private String notes;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Phase phase;

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Addition> getAdditions() {
        return additions;
    }

    public void setAdditions(List<Addition> additions) {
        this.additions = additions;
    }

    public void setOrdination(Ordination ordination) {
        this.ordination = ordination;
    }

    public Ordination getOrdination() {
        return ordination;
    }

    
}
