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
public class AdditionTest {
    
    private Addition addition;
    
    @Before
    public void setUp() {
        addition = new Addition();
    }

    @Test
    public void testSetName() {
        addition.setName("TEST");
        assertEquals("TEST", addition.getName());
    }

    @Test
    public void testSetPrice() {
        addition.setPrice(5.0f);
        assertEquals(5.0f, addition.getPrice(), 0.0f);
    }

    @Test
    public void testSetGeneric() {
        addition.setGeneric(true);
        assertTrue(addition.isGeneric());
    }
    
}
