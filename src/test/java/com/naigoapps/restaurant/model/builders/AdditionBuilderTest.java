/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Addition;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class AdditionBuilderTest {
    
    private static final float DELTA = 0.0f;
    
    @Test
    public void testName() {
        Addition result = new AdditionBuilder().name("name").getContent();
        assertEquals("name", result.getName());
    }

    @Test
    public void testPrice() {
        float price = 5.0f;
        Addition result = new AdditionBuilder().price(price).getContent();
        assertEquals(price, result.getPrice(), DELTA);
    }

    @Test
    public void testGeneric() {
        Addition a = new AdditionBuilder().generic(true).getContent();
        assertTrue(a.isGeneric());
    }

    @Test
    public void testGetContent() {
        Addition a = new AdditionBuilder().getContent();
        assertNotNull(a);
    }
    
}
