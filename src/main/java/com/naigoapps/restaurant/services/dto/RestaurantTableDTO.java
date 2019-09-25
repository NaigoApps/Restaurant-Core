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
public class RestaurantTableDTO extends DTO{
    private String name;
    private boolean busy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isBusy() {
		return busy;
	}
    
    public void setBusy(boolean busy) {
		this.busy = busy;
	}
}
