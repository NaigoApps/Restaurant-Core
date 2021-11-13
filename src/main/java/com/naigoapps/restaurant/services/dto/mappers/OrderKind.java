/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Order;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author naigo
 */
public class OrderKind {

    private final Dish orderDish;
    private final float orderPrice;
    private final Set<Addition> orderAdditions;
    private final String orderNotes;

    public OrderKind(Order o){
        this.orderDish = o.getDish();
        this.orderPrice = o.getPrice();
        this.orderAdditions = new HashSet<>(o.getAdditions());
        this.orderNotes = o.getNotes();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.orderDish);
        hash = 37 * hash + Float.floatToIntBits(this.orderPrice);
        hash = 37 * hash + Objects.hashCode(this.orderAdditions);
        hash = 37 * hash + Objects.hashCode(this.orderNotes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderKind other = (OrderKind) obj;
        if (Float.floatToIntBits(this.orderPrice) != Float.floatToIntBits(other.orderPrice)) {
            return false;
        }
        if (!Objects.equals(this.orderDish, other.orderDish)) {
            return false;
        }
        if (!Objects.equals(this.orderNotes, other.orderNotes)) {
            return false;
        }
        if(!this.orderAdditions.containsAll(other.orderAdditions)) {
        	return false;
        }
        return other.orderAdditions.containsAll(this.orderAdditions);
    }

    public Dish getOrderDish() {
        return orderDish;
    }

    public float getOrderPrice() {
        return orderPrice;
    }

    public Set<Addition> getOrderAdditions() {
        return orderAdditions;
    }

    public String getOrderNotes() {
        return orderNotes;
    }



}
