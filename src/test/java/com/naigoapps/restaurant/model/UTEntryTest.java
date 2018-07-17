/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class UTEntryTest {

    private UTEntry entry;
    
    @Before
    public void setUp(){
        entry = new UTEntry();
    }
    
    @Test
    public void testSetActor() {
        String user = "user";
        entry.setActor(user);
        assertEquals(user, entry.getActor());
    }

    @Test
    public void testSetActionName() {
        String action = "action";
        entry.setActionName(action);
        assertEquals(action, entry.getActionName());
    }

    @Test
    public void testSetActionTime() {
        LocalDateTime time = LocalDateTime.now();
        entry.setActionTime(time);
        assertEquals(time, entry.getActionTime());
    }

    @Test
    public void testSetBody() {
        String body = "body";
        entry.setActionBody(body);
        assertEquals(body, entry.getActionBody());
    }
    
}
