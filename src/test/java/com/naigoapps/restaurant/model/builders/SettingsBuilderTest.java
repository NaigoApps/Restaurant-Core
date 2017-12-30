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
public class SettingsBuilderTest {
    
    private SettingsBuilder builder;
    
    @Before
    public void setUp() {
        builder = new SettingsBuilder();
    }

    @Test
    public void testDefaultCoverCharge() {
        assertEquals(5.0f, builder.defaultCoverCharge(5.0f).getContent().getDefaultCoverCharge(), 0.0f);
    }

}
