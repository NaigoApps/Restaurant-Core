/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author naigo
 */
@Controller
@RequestMapping("/")
public class DiningTableWS {

    @Autowired
    private SimpMessagingTemplate template;

	public void update() {
        this.template.convertAndSend("/topic/dining-tables", "");
	}

}
