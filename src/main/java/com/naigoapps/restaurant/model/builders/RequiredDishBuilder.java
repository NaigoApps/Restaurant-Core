/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.RequiredDish;
import com.naigoapps.restaurant.services.dto.PhaseDTO;
import java.util.Collections;

/**
 *
 * @author naigo
 */
public class RequiredDishBuilder implements Builder<RequiredDish>{

    private Dish dish;
    private Ordination ordination;
    private float price;
    private Phase phase;
    
    public RequiredDishBuilder dish(Dish dish){
        this.dish = dish;
        price = dish.getPrice();
        return this;
    }
    
    public RequiredDishBuilder ordination(Ordination ordination){
        this.ordination = ordination;
        return this;
    }
    
    public RequiredDishBuilder price(float price){
        this.price = price;
        return this;
    }
    
    public RequiredDishBuilder phase(Phase phase){
        this.phase = phase;
        return this;
    }
    
    
    @Override
    public RequiredDish getContent() {
        RequiredDish result = new RequiredDish();
        result.setDish(dish);
        result.setOrdination(ordination);
        result.setPrice(price);
        result.setPhase(phase);
        result.setAdditions(Collections.EMPTY_LIST);
        return result;
    }
    
}
