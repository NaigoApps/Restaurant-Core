/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

/**
 *
 * @author naigo
 */
public class SettingsDTO extends DTO {

    private float defaultCoverCharge;
    private String mainPrinter;
    private String fiscalPrinter;
    private String clientSettings;
    private Boolean coverCharges;
    private Boolean shrinkOrdination;

    public SettingsDTO(String uuid, float defaultCoverCharge, String mainPrinter, String fiscalPrinter, Boolean coverCharges, Boolean shrinkOrdinations, String clientSettings) {
        super(uuid);
        this.defaultCoverCharge = defaultCoverCharge;
        this.mainPrinter = mainPrinter;
        this.fiscalPrinter = fiscalPrinter;
        this.coverCharges = coverCharges;
        this.shrinkOrdination = shrinkOrdinations;
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

    public Boolean getCoverCharges() {
        return coverCharges;
    }

    public Boolean getShrinkOrdination() {
        return shrinkOrdination;
    }

    public void setCoverCharges(Boolean coverCharges) {
        this.coverCharges = coverCharges;
    }

    public void setShrinkOrdination(Boolean shrinkOrdination) {
        this.shrinkOrdination = shrinkOrdination;
    }

}
