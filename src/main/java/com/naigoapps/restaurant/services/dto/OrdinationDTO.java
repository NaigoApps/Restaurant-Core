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
public class OrdinationDTO extends DTO{
    
    private String table;
    
    private LocalDateTime creationTime;
    
    private int progressive;
    
    private List<OrderDTO> orders;
    
    private boolean dirty;

    public OrdinationDTO() {
    }

    public OrdinationDTO(String table, LocalDateTime creationTime, int progressive, List<OrderDTO> orders, boolean dirty, String uuid) {
        super(uuid);
        this.table = table;
        this.creationTime = creationTime;
        this.progressive = progressive;
        this.orders = orders;
        this.dirty = dirty;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public int getProgressive() {
        return progressive;
    }

    public List<OrderDTO> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public boolean isDirty() {
        return dirty;
    }

    public String getTable() {
        return table;
    }

}
