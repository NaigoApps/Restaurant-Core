/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile;

import com.naigoapps.restaurant.model.DiningTableStatus;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author naigo
 */
public class DiningTableDTO extends DTO{

    private String eveningId;
    
    private int coverCharges;
    
    private WaiterDTO waiter;
    
    private List<OrdinationDTO> ordinations;
    
    private LocalDateTime openingTime;
    
    private RestaurantTableDTO table;
    
    private DiningTableStatus status;

    public DiningTableDTO() {}

    public String getEveningId() {
        return eveningId;
    }

    public void setEveningId(String eveningId) {
        this.eveningId = eveningId;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public void setCoverCharges(int coverCharges) {
        this.coverCharges = coverCharges;
    }

    public WaiterDTO getWaiter() {
        return waiter;
    }

    public void setWaiter(WaiterDTO waiter) {
        this.waiter = waiter;
    }

    public List<OrdinationDTO> getOrdinations() {
        return ordinations;
    }

    public void setOrdinations(List<OrdinationDTO> ordinations) {
        this.ordinations = ordinations;
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalDateTime openingTime) {
        this.openingTime = openingTime;
    }

    public RestaurantTableDTO getTable() {
        return table;
    }

    public void setTable(RestaurantTableDTO table) {
        this.table = table;
    }

    public DiningTableStatus getStatus() {
        return status;
    }

    public void setStatus(DiningTableStatus status) {
        this.status = status;
    }
    
    public static Comparator<DiningTableDTO> comparator(){
    	return (a, b) -> -a.getOpeningTime().compareTo(b.getOpeningTime());
    }
}
