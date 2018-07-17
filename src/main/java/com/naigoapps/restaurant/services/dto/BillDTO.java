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

    public void setProgressive(Integer progressive) {
        this.progressive = progressive;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public void setCoverCharges(int coverCharges) {
        this.coverCharges = coverCharges;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
    

}
