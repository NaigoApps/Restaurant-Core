/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 */
public class DiningTableBuilder implements Builder<DiningTable>{

    private DiningTableStatus status;
    private Evening evening;
    private LocalDateTime date;
    private Waiter waiter;
    private RestaurantTable table;
    private int coverCharges;
    private List<Ordination> ordinations;
    private List<Bill> bills;

    public DiningTableBuilder() {
        ordinations = new ArrayList<>();
        bills = new ArrayList<>();
    }
    
    
    
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
    
    public DiningTableBuilder status(DiningTableStatus value){
        this.status = value;
        return this;
    }
    
    public DiningTableBuilder bill(Bill bill){
        this.bills.add(bill);
        return this;
    }
    
    public DiningTableBuilder ordination(Ordination o){
        this.ordinations.add(o);
        return this;
    }
    
    @Override
    public DiningTable getContent() {
        DiningTable result = new DiningTable();
        result.setStatus(status != null ? status : DiningTableStatus.OPEN);
        result.setOpeningTime(date);
        result.setEvening(evening);
        result.setWaiter(waiter);
        result.setTable(table);
        result.setCoverCharges(coverCharges);
        result.setOrdinations(ordinations);
        result.setBills(bills);
        return result;
    }
    
}
