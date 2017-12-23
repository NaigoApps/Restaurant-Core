/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.main;

import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.dao.EveningDao;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author naigo
 */
@ApplicationScoped
public class EveningManager {

    private String selectedEveningUuid;

    @Inject
    EveningDao eveningDao;
    
    @PostConstruct
    public void init() {
        Logger.getLogger(this.getClass().getName()).info("***** CREATED_EVENING_MANAGER *****");
    }

    public Evening getSelectedEvening() {
        if(selectedEveningUuid != null){
            return eveningDao.findByUuid(selectedEveningUuid);
        }else{
            return null;
        }
    }

    public void setSelectedEvening(String selectedEveningUuid) {
        this.selectedEveningUuid = selectedEveningUuid;
    }

}
