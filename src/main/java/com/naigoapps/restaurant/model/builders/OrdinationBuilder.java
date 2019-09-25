/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;

/**
 *
 * @author naigo
 */
public class OrdinationBuilder implements Builder<Ordination>{

    private int progressive;
    private LocalDateTime creationTime;
    private DiningTable table;
    private boolean dirty;
    private List<Order> orders;

    public OrdinationBuilder() {
        orders = new ArrayList<>();
    }
    
    
    public OrdinationBuilder progressive(int progressive){
        this.progressive = progressive;
        return this;
    }
    
    public OrdinationBuilder creationTime(LocalDateTime time){
        this.creationTime = time;
        return this;
    }
    
    public OrdinationBuilder table(DiningTable table){
        this.table = table;
        return this;
    }
    
    public OrdinationBuilder dirty(boolean dirty){
        this.dirty = dirty;
        return this;
    }
    
    public OrdinationBuilder order(Order o){
        this.orders.add(o);
        return this;
    }
    
    @Override
    public Ordination getContent() {
        Ordination result = new Ordination();
        result.setProgressive(progressive);
        result.setCreationTime(creationTime);
        result.setTable(table);
        result.setDirty(this.dirty);
        result.setOrders(orders);
        return result;
    }
    
}
