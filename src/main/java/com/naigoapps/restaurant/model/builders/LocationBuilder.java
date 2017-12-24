/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;

/**
 *
 * @author naigo
 */
public class LocationBuilder implements Builder<Location>{

    private String name;
    private Printer printer;
    
    public LocationBuilder name(String value){
        this.name = value;
        return this;
    }
    
    public LocationBuilder printer(Printer value){
        this.printer = value;
        return this;
    }
    
    @Override
    public Location getContent() {
        Location result = new Location();
        result.setName(name);
        result.setPrinter(printer);
        return result;
    }
    
}
