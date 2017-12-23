/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "evenings")
public class Evening extends BaseEntity {

    private LocalDate day;

    private float coverCharge;

    @OneToMany(mappedBy = "evening")
    private List<DiningTable> diningTables;

    public Evening() {
        diningTables = new ArrayList<>();
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalDate getDay() {
        return day;
    }

    public List<DiningTable> getDiningTables() {
        return diningTables;
    }

    public void setDiningTables(List<DiningTable> diningTables) {
        this.diningTables = diningTables;
        diningTables.forEach(table -> {
            table.setEvening(this);
        });
    }
    
    public void addDiningTable(DiningTable table){
        if(!this.diningTables.contains(table)){
            this.diningTables.add(table);
            table.setEvening(this);
        }
    }

    public float getCoverCharge() {
        return coverCharge;
    }

    public void setCoverCharge(float coverCharge) {
        this.coverCharge = coverCharge;
    }

}
