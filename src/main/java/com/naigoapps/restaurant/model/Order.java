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
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    private Ordination ordination;

    @ManyToOne
    private Dish dish;

    @ManyToMany
    @JoinTable(name = "order_addition",
            joinColumns = {
                @JoinColumn(name = "order_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "addition_id")})
    private List<Addition> additions;

    private float price;

    private String notes;

    @ManyToOne
    private Phase phase;

    @ManyToOne
    private Bill bill;

    public Order() {
        additions = new ArrayList<>();
    }

    public void setBill(Bill bill) {
        this.bill = bill;
        if(bill != null){
            bill.addOrder(this);
        }
    }

    public Bill getBill() {
        return bill;
    }

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
    
    public void clearAdditions(){
        this.additions = new ArrayList<>();
    }

    public void setAdditions(List<Addition> additions) {
        this.additions = additions;
    }

    public void setOrdination(Ordination ordination) {
        this.ordination = ordination;
        if (ordination != null) {
            ordination.addOrder(this);
        }
    }

    public Ordination getOrdination() {
        return ordination;
    }

    public boolean isTheSame(Order other) {
        if (notes != null && other.notes == null ||
                notes == null && other.notes != null) {
            return false;
        }
        if(notes != null && other.notes != null && !notes.equals(other.notes)){
            return false;
        }
        
        if (dish != null && other.dish == null
                || dish == null && other.dish != null) {
            return false;
        }
        if (dish != null && other.dish != null && !dish.equals(other.dish)) {
            return false;
        }
        if (phase != null && other.phase == null
                || phase == null && other.phase != null) {
            return false;
        }
        if (phase != null && other.phase != null && !phase.equals(other.phase)) {
            return false;
        }
        if (!additions.stream()
                .noneMatch(a -> !other.additions.contains(a))) {
            return false;
        }
        if (!other.additions.stream()
                .noneMatch(a -> !additions.contains(a))) {
            return false;
        }
        if(price != other.price){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "1 x " + dish.getName();
    }

    
}
