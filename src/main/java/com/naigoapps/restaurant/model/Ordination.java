/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private LocalDateTime confirmTime;

    @ManyToOne
    private DiningTable table;
    
    @OneToMany(mappedBy = "ordination", fetch = FetchType.EAGER)
    private List<RequiredDish> orders;

    public Ordination() {
    }

    public void setProgressive(int progressive) {
        this.progressive = progressive;
    }

    public int getProgressive() {
        return progressive;
    }
    
    public void setOrders(List<RequiredDish> orders) {
        this.orders = orders;
    }

    public List<RequiredDish> getOrders() {
        return orders;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
    }

    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }
    
    

    public DiningTable getTable() {
        return table;
    }

    public void setTable(DiningTable table) {
        this.table = table;
    }
    
    
}
