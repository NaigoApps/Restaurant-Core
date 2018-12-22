/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "bills")
public class Bill extends BaseEntity {

    @ManyToOne
    private Customer customer;

    private LocalDateTime printTime;
    
    private LocalDate printDate;

    @ManyToOne
    private DiningTable table;

    @OneToMany(mappedBy = "bill")
    private List<Order> orders;

    private int coverCharges;

    private float total;

    private Integer progressive;

    public Bill() {
        orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        orders.forEach(order -> {
            order.setBill(this);
        });
    }

    public void removeOrder(Order o) {
        if (this.orders.contains(o)) {
            this.orders.remove(o);
            o.setBill(null);
        }
    }

    public void clearOrders() {
        this.orders.forEach(order -> {
            order.setBill(null);
        });
        this.orders.clear();
    }

    public void addOrder(Order order) {
        if (!this.orders.contains(order)) {
            this.orders.add(order);
            order.setBill(this);
        }
    }

    public void setTable(DiningTable table) {
        if (table == null && this.table != null) {
            this.table.getBills().remove(this);
        }
        this.table = table;
        if (table != null) {
            table.addBill(this);
        }
    }

    public DiningTable getTable() {
        return table;
    }

    public float getEstimatedTotal() {
        Float tot = orders.stream()
                .collect(Collectors.summingDouble(Order::getPrice)).floatValue();
        return tot + table.getEvening().getCoverCharge() * coverCharges;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public void setCoverCharges(int coverCharges) {
        this.coverCharges = coverCharges;
    }

    public Integer getProgressive() {
        return progressive;
    }

    public void setProgressive(Integer progressive) {
        this.progressive = progressive;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
        if(this.printTime != null){
            this.printDate = this.printTime.toLocalDate();
        }else{
            this.printDate = null;
        }
    }

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public LocalDate getPrintDate() {
        return printDate;
    }
    
    public boolean isGeneric(){
        return customer == null && printTime == null;
    }

}
