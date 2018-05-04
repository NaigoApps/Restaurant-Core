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
    private String surname;
    private String piva;
    private String cf;
    private String address;
    private String city;
    private String cap;
    private String district;
    
    public CustomerBuilder name(String value){
        this.name = value;
        return this;
    }
    
    public CustomerBuilder surname(String value){
        this.surname = value;
        return this;
    }
    
    public CustomerBuilder cf(String value){
        this.cf = value;
        return this;
    }
    
    public CustomerBuilder piva(String value){
        this.piva = value;
        return this;
    }
    
    public CustomerBuilder address(String value){
        this.address = value;
        return this;
    }
    
    public CustomerBuilder city(String value){
        this.city = value;
        return this;
    }
    
    public CustomerBuilder cap(String value){
        this.cap = value;
        return this;
    }
    
    public CustomerBuilder district(String value){
        this.district = value;
        return this;
    }
    
    @Override
    public Customer getContent() {
        Customer result = new Customer();
        result.setName(name);
        result.setSurname(surname);
        result.setAddress(address);
        result.setCap(cap);
        result.setCity(city);
        result.setDistrict(district);
        result.setCf(cf);
        result.setPiva(piva);
        return result;
    }
    
}
