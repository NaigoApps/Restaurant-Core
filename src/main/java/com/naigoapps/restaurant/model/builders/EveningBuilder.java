/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Evening;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author naigo
 */
public class EveningBuilder implements Builder<Evening>{

    private LocalDate day;
    private float coverCharge;

    public EveningBuilder() {
        this.coverCharge = Evening.DEFAULT_COVER_CHARGE;
    }
    
    public EveningBuilder day(LocalDate day){
        this.day = day;
        return this;
    }
    
    public EveningBuilder coverCharge(float val){
        this.coverCharge = val;
        return this;
    }
    
    @Override
    public Evening getContent() {
        Evening result = new Evening();
        result.setDay(day);
        result.setCoverCharge(coverCharge);
        result.setDiningTables(new ArrayList<>());
        return result;
    }
    
}
