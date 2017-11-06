/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 *
 * @author naigo
 */
@ApplicationScoped
public class WebSocketSessionHandler {

    private Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }
    
    public Set<Session> getSessions(){
        return Collections.unmodifiableSet(sessions);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    
}
