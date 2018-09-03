/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.utils;

import java.awt.Color;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class ColorAttributeConverterTest {
    
    private ColorAttributeConverter conv;
    
    @Before
    public void setUp() {
        conv = new ColorAttributeConverter();
    }

    @Test
    public void testConvertToDatabaseColumn() {
        assertEquals(Integer.valueOf(123), conv.convertToDatabaseColumn(new Color(123, true)));
    }

    @Test
    public void testConvertToEntityAttribute() {
        assertEquals(new Color(123, true), conv.convertToEntityAttribute(123));
    }
    
}
