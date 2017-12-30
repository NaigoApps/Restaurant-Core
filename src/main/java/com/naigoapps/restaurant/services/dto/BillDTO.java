/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author naigo
 */
public class BillDTO extends DTO{
    private String table;
    private List<String> orders;

    public BillDTO() {
    }

    public BillDTO(String uuid, String table, List<String> orders) {
        super(uuid);
        this.table = table;
        this.orders = orders;
    }

    public String getTable() {
        return table;
    }

    public List<String> getOrders() {
        return Collections.unmodifiableList(orders);
    }

}
