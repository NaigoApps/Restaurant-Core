/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 *
 * @author naigo
 */
public class DiningTableBuilder implements Builder<DiningTable>{

    private boolean closed;
    private Evening evening;
    private LocalDateTime date;
    private Waiter waiter;
    private RestaurantTable table;
    private int coverCharges;
    
    public DiningTableBuilder waiter(Waiter waiter){
        this.waiter = waiter;
        return this;
    }
    
    public DiningTableBuilder table(RestaurantTable table){
        this.table = table;
        return this;
    }
    
    public DiningTableBuilder ccs(int ccs){
        this.coverCharges = ccs;
        return this;
    }
    
    public DiningTableBuilder date(LocalDateTime date){
        this.date = date;
        return this;
    }
    
    public DiningTableBuilder evening(Evening evening){
        this.evening = evening;
        return this;
    }
    
    public DiningTableBuilder opened(){
        this.closed = false;
        return this;
    }
    
    public DiningTableBuilder closed(){
        this.closed = true;
        return this;
    }
    
    @Override
    public DiningTable getContent() {
        DiningTable result = new DiningTable();
        result.setClosed(closed);
        result.setDate(date);
        result.setEvening(evening);
        result.setWaiter(waiter);
        result.setTable(table);
        result.setCoverCharges(coverCharges);
        result.setOrdinations(Collections.EMPTY_LIST);
        return result;
    }
    
}
