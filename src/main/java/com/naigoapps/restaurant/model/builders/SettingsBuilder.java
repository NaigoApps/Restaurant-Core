/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Settings;

/**
 *
 * @author naigo
 */
public class SettingsBuilder implements Builder<Settings>{

    private float defaultCC;
    
    public SettingsBuilder defaultCoverCharge(float value){
        this.defaultCC = value;
        return this;
    }
    
    @Override
    public Settings getContent() {
        Settings result = new Settings();
        result.setDefaultCoverCharge(defaultCC);
        result.setClientSettings("{}");
        return result;
    }
    
}
