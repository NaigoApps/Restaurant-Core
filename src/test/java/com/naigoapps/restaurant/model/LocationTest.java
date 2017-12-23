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
public class LocationTest {
    
    private Location location;

    @Before
    public void setUp() {
        location = new Location();
    }

    @Test
    public void testSetName() {
        location.setName("name");
        assertEquals("name", location.getName());
    }

    @Test
    public void testSetPrinter() {
        Printer p = new Printer();
        location.setPrinter(p);
        assertEquals(p, location.getPrinter());
    }

}
