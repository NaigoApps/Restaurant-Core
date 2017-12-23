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
public class RestaurantTableTest {

    private RestaurantTable table;
    
    @Before
    public void setUp() {
        table = new RestaurantTable();
    }

    @Test
    public void testSetName() {
        table.setName("T1");
        assertEquals("T1", table.getName());
    }
    
}
