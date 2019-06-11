/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class OrdinationDTO extends DTO {

    private LocalDateTime creationTime;

    private List<PhaseOrdersDTO> orders;

    private boolean dirty;

    public OrdinationDTO() {
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public List<PhaseOrdersDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<PhaseOrdersDTO> orders) {
        this.orders = orders;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

}
