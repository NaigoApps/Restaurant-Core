/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
@Table(name = "dining_tables")
public class DiningTable extends BaseEntity{

    private int coverCharges;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Evening evening;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Waiter waiter;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "table", fetch = FetchType.EAGER)
    private List<Ordination> ordinations;
    
    private LocalDateTime openingTime;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private RestaurantTable table;

    private boolean closed;
    
    public DiningTable() {
        ordinations = new ArrayList<>();
    }
    
    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public List<Ordination> getOrdinations() {
        return ordinations;
    }

    public void setOrdinations(List<Ordination> orders) {
        this.ordinations = orders;
    }
    
    public void setCoverCharges(int coverCharges) {
        this.coverCharges = coverCharges;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public void setDate(LocalDateTime date) {
        this.openingTime = date;
    }

    public LocalDateTime getDate() {
        return openingTime;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public Evening getEvening() {
        return evening;
    }

    public void setEvening(Evening evening) {
        this.evening = evening;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }
    
    
    
    
    
}
