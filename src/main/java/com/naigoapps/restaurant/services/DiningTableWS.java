/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.services.websocket.SessionType;

/**
 *
 * @author naigo
 */
@Transactional
@ServerEndpoint("/dining-tables")
public class DiningTableWS {
 
    @Inject
    private EveningManager manager;
    
    @OnOpen
    public void openSession(Session session) {
        manager.addSession(SessionType.DINING_TABLES.name(), session);
    }
    
    public void update(){
        manager.updateSessions(SessionType.DINING_TABLES.name());
    }
 
    @OnClose
    public void closeSession(Session session, CloseReason reason) {
        manager.removeSession(session);
    }
    
}
