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
public class BillDTO extends DTO{
    private Integer progressive;
    private String customer;
    private LocalDateTime printTime;
    private String table;
    private List<String> orders;
    private int coverCharges;
    private float total;

    public BillDTO() {
    }

    public BillDTO(Integer progressive, String customer, LocalDateTime printTime, String table, List<String> orders, int coverCharges, float total, String uuid) {
        super(uuid);
        this.progressive = progressive;
        this.customer = customer;
        this.printTime = printTime;
        this.table = table;
        this.orders = orders;
        this.coverCharges = coverCharges;
        this.total = total;
    }

    
    
    public String getTable() {
        return table;
    }

    public List<String> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public float getTotal() {
        return total;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public String getCustomer() {
        return customer;
    }

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public Integer getProgressive() {
        return progressive;
    }
    
    

}
