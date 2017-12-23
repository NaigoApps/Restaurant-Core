/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class WaiterTest {
    
    private Waiter waiter;
    
    @Before
    public void setUp() {
        waiter = new Waiter();
    }

    @Test
    public void testSetName() {
        waiter.setName("name");
        assertEquals("name", waiter.getName());
    }

    @Test
    public void testSetSurname() {
        waiter.setSurname("surname");
        assertEquals("surname", waiter.getSurname());
    }

    @Test
    public void testSetStatus() {
        waiter.setStatus(WaiterStatus.REMOVED);
        assertEquals(WaiterStatus.REMOVED, waiter.getStatus());
    }

    @Test
    public void testSetCf() {
        waiter.setCf("cf");
        assertEquals("cf", waiter.getCf());
    }
    
}
