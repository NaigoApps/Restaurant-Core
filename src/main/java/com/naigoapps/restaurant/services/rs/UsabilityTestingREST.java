/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.UTEntry;
import com.naigoapps.restaurant.model.dao.UTDao;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/ut")
@RestController
@Transactional
public class UsabilityTestingREST {

    @Autowired
    UTDao utDao;

    @PostMapping
    public void completeAction(@RequestParam("user") String user, @RequestParam("action") String action, @RequestBody String body) {

        UTEntry entry = new UTEntry();
        entry.setActor(user);
        entry.setActionName(action);
        entry.setActionBody(body);
        entry.setActionTime(LocalDateTime.now());
        
        utDao.persist(entry);
    }

}
