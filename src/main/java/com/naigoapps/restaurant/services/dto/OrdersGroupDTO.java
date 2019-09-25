/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.naigoapps.restaurant.model.Order;

/**
 *
 * @author naigo
 */
public class OrdersGroupDTO extends DTO {

	private String phaseName;
	
    private DishDTO dish;

    private List<AdditionDTO> additions;

    private int quantity;

    private float price;

    private String notes;

    private List<OrderDTO> orders;
    
    public OrdersGroupDTO() {
    	orders = new ArrayList<>();
    }
    
    public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
    
    public String getPhaseName() {
		return phaseName;
	}
    
    public DishDTO getDish() {
        return dish;
    }

    public void setDish(DishDTO dish) {
        this.dish = dish;
    }

    public List<AdditionDTO> getAdditions() {
        return additions;
    }

    public void setAdditions(List<AdditionDTO> additions) {
        this.additions = additions;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public List<OrderDTO> getOrders() {
		return orders;
	}
    
    public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}

    public Set<String> getAdditionUuids(){
    	return additions.stream()
    			.map(DTO::getUuid)
    			.collect(Collectors.toSet());
    }
    
    public boolean matches(Order other) {
        if (Float.floatToIntBits(this.price) != Float.floatToIntBits(other.getPrice())) {
            return false;
        }
        if (!Objects.equals(this.dish.getUuid(), other.getDish().getUuid())) {
            return false;
        }
        if (!Objects.equals(this.notes, other.getNotes())) {
            return false;
        }
        
        Set<String> a1 = additions.stream().map(AdditionDTO::getUuid).collect(Collectors.toSet());
        Set<String> a2 = other.getAdditionUuids();
        
        return a1.equals(a2);
    }
}
