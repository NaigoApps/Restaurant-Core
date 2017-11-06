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
public class OrdinationDTO extends DTO{
    
    private String diningTable;

    private LocalDateTime creationTime;
    private LocalDateTime confirmTime;
    
    private String table;
    
    private List<RequiredDishDTO> orders;
    
    public OrdinationDTO() {
    }

    public OrdinationDTO(String uuid, String diningTable, LocalDateTime creationTime, LocalDateTime confirmTime, String table, List<RequiredDishDTO> orders) {
        super(uuid);
        this.diningTable = diningTable;
        this.creationTime = creationTime;
        this.confirmTime = confirmTime;
        this.table = table;
        this.orders = orders;
    }

    public String getDiningTable() {
        return diningTable;
    }

    public void setDiningTable(String diningTable) {
        this.diningTable = diningTable;
    }
    
    

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
    }

    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<RequiredDishDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<RequiredDishDTO> orders) {
        this.orders = orders;
    }

    
}
