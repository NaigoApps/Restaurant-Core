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
public class PhaseBuilderTest {
    
    private PhaseBuilder builder;

    @Before
    public void setUp() {
        builder = new PhaseBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testPriority() {
        assertEquals(3, builder.priority(3).getContent().getPriority());
    }

}
