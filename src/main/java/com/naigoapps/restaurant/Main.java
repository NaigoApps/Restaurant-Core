/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.naigoapps.restaurant.model.dao.Generic;
import com.naigoapps.restaurant.model.dao.GenericDao;

/**
 *
 * @author naigo
 */
@Startup
@Singleton
public class Main {

    @Inject @Generic
    private GenericDao dao;

    @PostConstruct
    public void init() {
//        String chromePath = ResourceBundle.getBundle("properties").getString("CHROME");
//        
//        try {
//            Process p = Runtime.getRuntime().exec(chromePath + " http://localhost:8080/restaurant");
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.WARNING, null, ex);
//        }
    }
}
