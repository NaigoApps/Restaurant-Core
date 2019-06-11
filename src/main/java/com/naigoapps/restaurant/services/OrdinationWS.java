/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.naigoapps.restaurant.main.EveningManager;

/**
 *
 * @author naigo
 */
@ServerEndpoint("/ordinations/{tableUuid}")
public class OrdinationWS {
 
    @Inject
    private EveningManager manager;
    
    @OnOpen
    public void openSession(@PathParam("tableUuid") String tableUuid, Session session) {
        manager.addSession(tableUuid, session);
    }
    
    public void update(String tableUuid){
        manager.updateSessions(tableUuid);
    }
 
    @OnClose
    public void closeSession(Session session, CloseReason reason) {
        manager.removeSession(session);
    }
    
}
