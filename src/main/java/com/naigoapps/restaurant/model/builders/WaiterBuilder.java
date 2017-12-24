/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;

/**
 *
 * @author naigo
 */
public class WaiterBuilder implements Builder<Waiter>{

    private String name;
    private String surname;
    private String cf;
    private WaiterStatus status;
    
    public WaiterBuilder name(String name){
        this.name = name;
        return this;
    }
    
    public WaiterBuilder surname(String surname){
        this.surname = surname;
        return this;
    }
    
    public WaiterBuilder cf(String cf){
        this.cf = cf;
        return this;
    }
    
    public WaiterBuilder status(WaiterStatus status){
        this.status = status;
        return this;
    }
    
    @Override
    public Waiter getContent() {
        Waiter result = new Waiter();
        result.setName(name);
        result.setSurname(surname);
        result.setCf(cf);
        result.setStatus(status != null ? status : WaiterStatus.ACTIVE);
        return result;
    }
    
}
