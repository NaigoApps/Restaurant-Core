/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "ordinations")
public class Ordination extends BaseEntity{

    private int progressive;
    
    private LocalDateTime creationTime;

    @ManyToOne
    private DiningTable table;
    
    @OneToMany(mappedBy = "ordination")
    private List<Order> orders;
    
    private boolean dirty;

    public Ordination() {
        orders = new ArrayList<>();
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setProgressive(int progressive) {
        this.progressive = progressive;
    }

    public int getProgressive() {
        return progressive;
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders;
        orders.forEach(order -> {
            order.setOrdination(this);
        });
    }
    
    public void addOrder(Order order){
        if(!this.orders.contains(order)){
            this.orders.add(order);
            order.setOrdination(this);
        }
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public DiningTable getTable() {
        return table;
    }

    public void setTable(DiningTable table) {
        this.table = table;
        table.addOrdination(this);
    }
    
    
}
