/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "printers")
public class Printer extends BaseEntity {

    private String name;

    private boolean main;

    private int lineCharacters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isMain() {
        return main;
    }

    public int getLineCharacters() {
        return lineCharacters;
    }

    public void setLineCharacters(int lineCharacters) {
        this.lineCharacters = lineCharacters;
    }

}
