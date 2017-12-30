/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author naigo
 */
public class DiningTableDTO extends DTO{

    private final int coverCharges;
    
    private final String waiter;
    
    private final List<OrdinationDTO> ordinations;
    
    private final List<BillDTO> bills;
    
    private final LocalDateTime openingTime;
    
    private final String table;
    
    private final boolean closed;

    public DiningTableDTO() {
        this.coverCharges = 0;
        this.waiter = null;
        this.ordinations = null;
        this.bills = null;
        this.openingTime = null;
        this.table = null;
        this.closed = false;
    }

    public DiningTableDTO(int coverCharges, String waiter, List<OrdinationDTO> ordinations, List<BillDTO> bills, LocalDateTime openingTime, String table, boolean closed, String uuid) {
        super(uuid);
        this.coverCharges = coverCharges;
        this.waiter = waiter;
        this.ordinations = ordinations;
        this.bills = bills;
        this.openingTime = openingTime;
        this.table = table;
        this.closed = closed;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public String getWaiter() {
        return waiter;
    }

    public List<OrdinationDTO> getOrdinations() {
        return Collections.unmodifiableList(ordinations);
    }

    public List<BillDTO> getBills() {
        return Collections.unmodifiableList(bills);
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public String getTable() {
        return table;
    }

    public boolean isClosed() {
        return closed;
    }

}
