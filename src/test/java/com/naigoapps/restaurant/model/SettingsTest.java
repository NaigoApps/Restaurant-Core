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
public class SettingsTest {
    
    private Settings settings;
    
    @Before
    public void setUp() {
        settings = new Settings();
    }

    @Test
    public void testSetDefaultCoverCharge() {
        settings.setDefaultCoverCharge(5);
        assertEquals(5.0f, settings.getDefaultCoverCharge(), 0.0f);
    }
    
}
