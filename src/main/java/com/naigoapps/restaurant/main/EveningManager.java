/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.services.DiningTableWS;
import com.naigoapps.restaurant.services.websocket.SessionType;

/**
 *
 * @author naigo
 */
@ApplicationScoped
public class EveningManager {

    private String selectedEveningUuid;

    private Map<String, Set<Session>> sessions;

    @Inject
    private EveningDao eveningDao;
    
    @PostConstruct
    public void init() {
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

    public void addSession(String key, Session s) {
		Set<Session> set = this.sessions.computeIfAbsent(key, k -> new HashSet<>());
		set.add(s);
    }

    public void removeSession(Session s) {
        this.sessions.entrySet().forEach(entry -> {
            if(entry.getValue().contains(s)){
                entry.getValue().remove(s);
            }
        });
    }

    public void updateSessions(String key) {
        Set<Session> toRemove = new HashSet<>();
        Set<Session> toSend = sessions.get(key);
        if(toSend != null){
            toSend.stream().forEach(session -> {
                try {
                    session.getBasicRemote().sendText(buildMessage(key));
                } catch (Exception ex) {
                    Logger.getLogger(DiningTableWS.class.getName()).log(Level.SEVERE, null, ex);
                    toRemove.add(session);
                }
            });
            toSend.removeAll(toRemove);
        }
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
