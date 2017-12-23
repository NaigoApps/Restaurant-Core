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
    
    private LocalDateTime creationTime;
    
    private boolean dirty;
    
    private String table;
    
    private List<OrderDTO> orders;
    
    public OrdinationDTO() {
    }

    public OrdinationDTO(String uuid, String table, LocalDateTime creationTime, List<OrderDTO> orders, boolean dirty) {
        super(uuid);
        this.creationTime = creationTime;
        this.table = table;
        this.orders = orders;
        this.dirty = dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }
    
    

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    
}
