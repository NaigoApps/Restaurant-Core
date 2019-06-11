/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile;

import java.util.List;

/**
 *
 * @author naigo
 */
public class PhaseOrdersDTO extends DTO {

    private PhaseDTO phase;

    private List<OrdersGroupDTO> orders;

    public PhaseOrdersDTO() {
    }

    public PhaseDTO getPhase() {
        return phase;
    }

    public void setPhase(PhaseDTO phase) {
        this.phase = phase;
    }

    public List<OrdersGroupDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersGroupDTO> orders) {
        this.orders = orders;
    }

}
