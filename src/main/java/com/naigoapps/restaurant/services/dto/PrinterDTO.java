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
    
    private boolean main;
    
    private int lineCharacters;

    public PrinterDTO() {
    }

    public PrinterDTO(String uuid, String name, boolean main, int lineCharacters) {
        super(uuid);
        this.name = name;
        this.main = main;
        this.lineCharacters = lineCharacters;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isMain() {
        return main;
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
