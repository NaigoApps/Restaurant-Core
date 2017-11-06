/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

/**
 *
 * @author naigo
 */
public class LocationDTO extends DTO{
    private String name;
    
    private String printer;

    public LocationDTO() {
    }

    public LocationDTO(String uuid, String name, String printer) {
        super(uuid);
        this.name = name;
        this.printer = printer;
    }

    
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getPrinter() {
        return printer;
    }
    
    
}
