/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import com.naigoapps.restaurant.model.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
public class SettingsDTO extends DTO{

    private float defaultCoverCharge;
    private String mainPrinter;
    private String fiscalPrinter;
    private String clientSettings;

    public SettingsDTO(String uuid, float defaultCoverCharge, String mainPrinter, String fiscalPrinter, String clientSettings) {
        super(uuid);
        this.defaultCoverCharge = defaultCoverCharge;
        this.mainPrinter = mainPrinter;
        this.fiscalPrinter = fiscalPrinter;
        this.clientSettings = clientSettings;
    }
    
    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public float getDefaultCoverCharge() {
        return defaultCoverCharge;
    }

    public void setDefaultCoverCharge(float defaultCoverCharge) {
        this.defaultCoverCharge = defaultCoverCharge;
    }

    public void setFiscalPrinter(String fiscalPrinter) {
        this.fiscalPrinter = fiscalPrinter;
    }

    public void setMainPrinter(String mainPrinter) {
        this.mainPrinter = mainPrinter;
    }

    public String getFiscalPrinter() {
        return fiscalPrinter;
    }

    public String getMainPrinter() {
        return mainPrinter;
    }

    
    

}
