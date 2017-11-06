/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.naigoapps.restaurant.services.serializers.WaiterStatusDeserializer;
import com.naigoapps.restaurant.services.serializers.WaiterStatusSerializer;

/**
 *
 * @author naigo
 */
@JsonSerialize(using = WaiterStatusSerializer.class)
@JsonDeserialize(using = WaiterStatusDeserializer.class)
public enum WaiterStatus {
    ACTIVE("ATTIVO"), SUSPENDED("SOSPESO"), REMOVED("RIMOSSO");
    
    private String name;
    
    WaiterStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public static WaiterStatus fromName(String name){
        WaiterStatus result = null;
        for(WaiterStatus status : WaiterStatus.values()){
            if(status.getName().equals(name)){
                result = status;
            }
        }
        return result;
    }
    
}
