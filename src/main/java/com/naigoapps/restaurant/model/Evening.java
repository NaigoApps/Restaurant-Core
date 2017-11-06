/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "evenings")
public class Evening extends BaseEntity{

    public static float DEFAULT_COVER_CHARGE = 1.0f;
    
    private LocalDate day;
    
    private float coverCharge;
    
    @OneToMany(mappedBy = "evening", fetch = FetchType.EAGER)
    private List<DiningTable> diningTables;

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
    }

    public float getCoverCharge() {
        return coverCharge;
    }

    public void setCoverCharge(float coverCharge) {
        this.coverCharge = coverCharge;
    }

    public void addDiningTable(DiningTable diningTable) {
        this.diningTables.add(diningTable);
        diningTable.setEvening(this);
    }
    
}
