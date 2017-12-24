/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Phase;

/**
 *
 * @author naigo
 */
public class PhaseBuilder implements Builder<Phase>{

    private String name;
    private int priority;
    
    public PhaseBuilder name(String name){
        this.name = name;
        return this;
    }
    
    public PhaseBuilder priority(int value){
        this.priority = value;
        return this;
    }
    
    @Override
    public Phase getContent() {
        Phase result = new Phase();
        result.setName(name);
        result.setPriority(priority);
        return result;
    }
    
}
