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
public class PrinterTest {
    
    private Printer printer;

    @Before
    public void setUp() {
        printer = new Printer();
    }

    @Test
    public void testSetName() {
        printer.setName("name");
        assertEquals("name", printer.getName());
    }

    @Test
    public void testSetLineCharacters() {
        printer.setLineCharacters(10);
        assertEquals(10, printer.getLineCharacters());
    }
    
}
