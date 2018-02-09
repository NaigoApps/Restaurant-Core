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
public class PhaseDTO extends DTO{
    private String name;
    private int priority;

    public PhaseDTO() {
        super();
    }

    public PhaseDTO(String uuid, String name, int priority) {
        super(uuid);
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    
    
}
