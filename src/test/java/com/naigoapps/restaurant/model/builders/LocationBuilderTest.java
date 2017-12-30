/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Printer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class LocationBuilderTest {
    
    private LocationBuilder builder;
    
    @Before
    public void setUp() {
        builder = new LocationBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testPrinter() {
        Printer printer = new PrinterBuilder().getContent();
        assertEquals(printer, builder.printer(printer).getContent().getPrinter());
    }

}
