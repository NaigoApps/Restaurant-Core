/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Order;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 */
public class OrderBuilder implements Builder<Order>{

    private Dish dish;
    private Ordination ordination;
    private float price;
    private String notes;
    private Phase phase;
    private List<Addition> additions;

    public OrderBuilder() {
        additions = new ArrayList<>();
    }
    
    public OrderBuilder dish(Dish dish){
        this.dish = dish;
        price = dish.getPrice();
        return this;
    }
    
    public OrderBuilder ordination(Ordination ordination){
        this.ordination = ordination;
        return this;
    }
    
    public OrderBuilder price(float price){
        this.price = price;
        return this;
    }
    
    public OrderBuilder notes(String notes){
        this.notes = notes;
        return this;
    }
    
    public OrderBuilder phase(Phase phase){
        this.phase = phase;
        return this;
    }
    
    public OrderBuilder addition(Addition a){
        this.additions.add(a);
        return this;
    }
    
    @Override
    public Order getContent() {
        Order result = new Order();
        result.setDish(dish);
        result.setOrdination(ordination);
        result.setPrice(price);
        result.setNotes(notes);
        result.setPhase(phase);
        result.setAdditions(additions);
        return result;
    }
    
}
