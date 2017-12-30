/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Addition;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author naigo
 */
public class AdditionBuilderTest {
    
    private AdditionBuilder builder;

    @Before
    public void setUp() {
        builder = new AdditionBuilder();
    }
    
    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testPrice() {
        assertEquals(5.0f, builder.price(5.0f).getContent().getPrice(), 0.0f);
    }

    @Test
    public void testGeneric() {
        assertTrue(builder.generic(true).getContent().isGeneric());
    }

}
