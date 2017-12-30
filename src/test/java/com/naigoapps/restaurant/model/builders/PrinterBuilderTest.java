/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class PrinterBuilderTest {

    private PrinterBuilder builder;
    
    @Before
    public void setUp() {
        builder = new PrinterBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testMain() {
        assertTrue(builder.main(true).getContent().isMain());
    }

    @Test
    public void testLine() {
        assertEquals(10, builder.line(10).getContent().getLineCharacters());
    }

}
