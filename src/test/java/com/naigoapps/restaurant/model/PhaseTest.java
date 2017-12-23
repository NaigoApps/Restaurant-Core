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
public class PhaseTest {
    
    private Phase phase;
    
    @Before
    public void setUp() {
        phase = new Phase();
    }

    @Test
    public void testSetName() {
        phase.setName("name");
        assertEquals("name", phase.getName());
    }

    @Test
    public void testSetPriority() {
        phase.setPriority(5);
        assertEquals(5, phase.getPriority());
    }
    
}
