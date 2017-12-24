/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 */
public class EveningBuilder implements Builder<Evening>{

    private LocalDate day;
    private float coverCharge;
    private List<DiningTable> diningTables;

    public EveningBuilder() {
        diningTables = new ArrayList<>();
    }
    
    public EveningBuilder day(LocalDate day){
        this.day = day;
        return this;
    }
    
    public EveningBuilder coverCharge(float val){
        this.coverCharge = val;
        return this;
    }
    
    public EveningBuilder diningTable(DiningTable table){
        this.diningTables.add(table);
        return this;
    }
    
    @Override
    public Evening getContent() {
        Evening result = new Evening();
        result.setDay(day);
        result.setCoverCharge(coverCharge);
        result.setDiningTables(diningTables);
        return result;
    }
    
}
