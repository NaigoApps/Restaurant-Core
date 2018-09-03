/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Printer;

/**
 *
 * @author naigo
 */
public class PrinterBuilder implements Builder<Printer>{

    private String name;
    private int lineCharacters;
    
    public PrinterBuilder name(String value){
        this.name = value;
        return this;
    }
    
    public PrinterBuilder line(int value){
        this.lineCharacters = value;
        return this;
    }
    
    @Override
    public Printer getContent() {
        Printer result = new Printer();
        result.setLineCharacters(lineCharacters);
        result.setName(name);
        return result;
    }
    
}
