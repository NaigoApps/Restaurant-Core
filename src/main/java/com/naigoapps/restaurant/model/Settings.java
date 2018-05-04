/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "settings")
public class Settings extends BaseEntity {

    private float defaultCoverCharge;
    
    private String clientSettings;

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

}
