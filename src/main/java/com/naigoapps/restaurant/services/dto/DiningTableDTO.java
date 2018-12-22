/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import com.naigoapps.restaurant.model.DiningTableStatus;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author naigo
 */
public class DiningTableDTO extends DTO{

    private final String evening;
    
    private final int coverCharges;
    
    private final String waiter;
    
    private final List<String> ordinations;
    
    private final List<String> bills;
    
    private final LocalDateTime openingTime;
    
    private final String table;
    
    private final DiningTableStatus status;

    public DiningTableDTO() {
        this.coverCharges = 0;
        this.evening = null;
        this.waiter = null;
        this.ordinations = null;
        this.bills = null;
        this.openingTime = null;
        this.table = null;
        this.status = null;
    }

    public DiningTableDTO(String evening, int coverCharges, String waiter, List<String> ordinations, List<String> bills, LocalDateTime openingTime, String table, DiningTableStatus status, String uuid) {
        super(uuid);
        this.evening = evening;
        this.coverCharges = coverCharges;
        this.waiter = waiter;
        this.ordinations = ordinations;
        this.bills = bills;
        this.openingTime = openingTime;
        this.table = table;
        this.status = status;
    }

    public String getEvening() {
        return evening;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public String getWaiter() {
        return waiter;
    }

    public List<String> getOrdinations() {
        return Collections.unmodifiableList(ordinations);
    }

    public List<String> getBills() {
        return Collections.unmodifiableList(bills);
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public String getTable() {
        return table;
    }

    public DiningTableStatus getStatus() {
        return status;
    }

    

}
