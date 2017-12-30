/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author naigo
 */
public class EveningDTO extends DTO{

    private LocalDate day;
    
    private float coverCharge;
    
    private List<DiningTableDTO> diningTables;

    public EveningDTO() {
    }

    public EveningDTO(String uuid, LocalDate day, float coverCharge, List<DiningTableDTO> diningTables) {
        super(uuid);
        this.day = day;
        this.coverCharge = coverCharge;
        this.diningTables = new ArrayList<>(diningTables);
    }
    
    public LocalDate getDay() {
        return day;
    }

    public List<DiningTableDTO> getDiningTables() {
        return Collections.unmodifiableList(diningTables);
    }

    public float getCoverCharge() {
        return coverCharge;
    }

}
