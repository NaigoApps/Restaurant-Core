/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author naigo
 */
@Entity
@DiscriminatorValue("receipt")
public class Receipt extends Bill {

    private LocalDateTime printTime;

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    
}
