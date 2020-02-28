/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.Session;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.services.websocket.SessionType;

/**
 *
 * @author naigo
 */

@Component
public class EveningManager {

    private String selectedEveningUuid;

    private Map<String, Set<Session>> sessions;

    @Autowired
    private EveningDao eveningDao;
    
    @Autowired
    private CamelContext context;
    
    public EveningManager() {
    	Logger.getLogger(this.getClass().getName()).info("***** CREATED_EVENING_MANAGER *****");
    	sessions = new HashMap<>();
	}
    
    public Evening getSelectedEvening() {
        if (selectedEveningUuid != null) {
            return eveningDao.findByUuid(selectedEveningUuid);
        } else {
            return null;
        }
    }

    public void setSelectedEvening(String selectedEveningUuid) {
        this.selectedEveningUuid = selectedEveningUuid;
    }
    
    private String buildMessage(String key){
        if(key.equals(SessionType.DINING_TABLES.name())){
            return buildDiningTablesMessage();
        }else{
            return buildOrdinationsMessage(key);
        }
    }
    
    private String buildDiningTablesMessage(){
    	return selectedEveningUuid;
    }
    
    private String buildOrdinationsMessage(String tableUuid){
    	return tableUuid;
    }
}
