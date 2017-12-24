/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Customer;

/**
 *
 * @author naigo
 */
public class CustomerBuilder implements Builder<Customer>{

    private String name;
    
    public CustomerBuilder name(String name){
        this.name = name;
        return this;
    }
    
    @Override
    public Customer getContent() {
        Customer result = new Customer();
        result.setName(name);
        return result;
    }
    
}
