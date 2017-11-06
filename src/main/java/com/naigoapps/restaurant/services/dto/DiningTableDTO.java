/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class DiningTableDTO extends DTO{

    private int coverCharges;
    
    private String waiter;
    
    private List<String> ordinations;
    
    private LocalDateTime openingTime;
    
    private String table;

    public DiningTableDTO() {
    }

    
    public DiningTableDTO(String uuid, int coverCharges, String waiter, List<String> ordinations, LocalDateTime openingTime, String table) {
        super(uuid);
        this.coverCharges = coverCharges;
        this.waiter = waiter;
        this.ordinations = ordinations;
        this.openingTime = openingTime;
        this.table = table;
    }

    
    
    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public List<String> getOrdinations() {
        return ordinations;
    }

    public void setOrdinations(List<String> ordinations) {
        this.ordinations = ordinations;
    }
    
    public void setCoverCharges(int coverCharges) {
        this.coverCharges = coverCharges;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public void setDate(LocalDateTime date) {
        this.openingTime = date;
    }

    public LocalDateTime getDate() {
        return openingTime;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    
}
