/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.websocket;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author naigo
 */



@ServerEndpoint("/notifications")
public class WebSocketServer {

    @Inject
    WebSocketSessionHandler handler;
    
    @OnOpen
    public void open(Session session) {
        System.out.println("OPEN " + session.getId());
        handler.addSession(session);
    }
    
    public void broadcastMessage(String message){
        handler.getSessions().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }

    @OnClose
    public void close(Session session) {
        System.out.println("CLOSE " + session.getId());
        handler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("ERROR " + error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println("MESSAGE " + message + " ON " + session.getId());
    }

}
