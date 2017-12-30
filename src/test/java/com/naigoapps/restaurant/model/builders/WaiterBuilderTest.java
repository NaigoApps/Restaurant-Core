/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.WaiterStatus;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class WaiterBuilderTest {
    
    private WaiterBuilder builder;
    
    @Before
    public void setUp() {
        builder = new WaiterBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testSurname() {
        assertEquals("surname", builder.name("surname").getContent().getName());
    }

    @Test
    public void testCf() {
        assertEquals("cf", builder.name("cf").getContent().getName());
    }

    @Test
    public void testDefaultStatus() {
        assertEquals(WaiterStatus.ACTIVE, builder.getContent().getStatus());
    }
    
    @Test
    public void testStatus() {
        assertEquals(WaiterStatus.REMOVED, builder.status(WaiterStatus.REMOVED).getContent().getStatus());
    }

}
