/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "settings")
public class Settings extends BaseEntity {

    private float defaultCoverCharge;

    @ManyToOne
    private Printer mainPrinter;

    @ManyToOne
    private Printer fiscalPrinter;

    @Column(length = 4096)
    private String clientSettings;

    private Boolean useCoverCharges;

    private Boolean shrinkOrdinations;

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

    public Printer getFiscalPrinter() {
        return fiscalPrinter;
    }

    public Printer getMainPrinter() {
        return mainPrinter;
    }

    public void setFiscalPrinter(Printer fiscalPrinter) {
        this.fiscalPrinter = fiscalPrinter;
    }

    public void setMainPrinter(Printer mainPrinter) {
        this.mainPrinter = mainPrinter;
    }

    public Boolean getShrinkOrdinations() {
        return shrinkOrdinations;
    }

    public Boolean getUseCoverCharges() {
        return useCoverCharges;
    }

    public void setShrinkOrdinations(Boolean shrinkOrdinations) {
        this.shrinkOrdinations = shrinkOrdinations;
    }

    public void setUseCoverCharges(Boolean useCoverCharges) {
        this.useCoverCharges = useCoverCharges;
    }

}
