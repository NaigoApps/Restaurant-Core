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
public class BaseEntityTest {
    
    BaseEntity entity;
    
    @Before
    public void setUp() {
        entity = new BaseEntity();
    }

    @Test
    public void testSetId() {
        entity.setId(5);
        assertEquals(5, entity.getId());
    }

    @Test
    public void testHasUuid() {
        assertNotNull(entity.getUuid());
    }

    @Test
    public void testSetUuid() {
        entity.setUuid("uuid");
        assertEquals("uuid", entity.getUuid());
    }

}
