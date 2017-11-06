/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Ordination;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 *
 * @author naigo
 */
public class OrdinationBuilder implements Builder<Ordination>{

    int progressive;
    LocalDateTime creationTime;
    DiningTable table;
    
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
    
    @Override
    public Ordination getContent() {
        Ordination result = new Ordination();
        result.setProgressive(progressive);
        result.setCreationTime(creationTime);
        result.setTable(table);
        result.setOrders(Collections.EMPTY_LIST);
        return result;
    }
    
}
