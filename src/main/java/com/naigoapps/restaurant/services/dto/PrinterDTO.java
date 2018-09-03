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
public class PrinterDTO  extends DTO{

    private String name;
    
    private int lineCharacters;

    public PrinterDTO() {
        super();
    }

    public PrinterDTO(String uuid, String name, int lineCharacters) {
        super(uuid);
        this.name = name;
        this.lineCharacters = lineCharacters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLineCharacters(int lineCharacters) {
        this.lineCharacters = lineCharacters;
    }

    public int getLineCharacters() {
        return lineCharacters;
    }

    
}
