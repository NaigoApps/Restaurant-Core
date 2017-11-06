/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author naigo
 */
public class EveningDTO extends DTO{

    private LocalDate day;
    
    private float coverCharge;
    
    private List<DiningTableDTO> diningTables;

    public EveningDTO(String uuid, LocalDate day, float coverCharge, List<DiningTableDTO> diningTables) {
        super(uuid);
        this.day = day;
        this.coverCharge = coverCharge;
        this.diningTables = diningTables;
    }

    
    
    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalDate getDay() {
        return day;
    }

    public List<DiningTableDTO> getDiningTables() {
        return diningTables;
    }

    public void setDiningTables(List<DiningTableDTO> diningTables) {
        this.diningTables = diningTables;
    }

    public float getCoverCharge() {
        return coverCharge;
    }

    public void setCoverCharge(float coverCharge) {
        this.coverCharge = coverCharge;
    }
    
    
}
