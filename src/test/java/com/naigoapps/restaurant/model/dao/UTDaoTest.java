/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.UTEntry;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class UTDaoTest {
    
    private UTDao dao;
    
    @Before
    public void setUp() {
        dao = new UTDao();
    }

    @Test
    public void testGetEntityClass() {
        assertEquals(UTEntry.class, dao.getEntityClass());
    }

    @Test
    public void testGetOrderByClause() {
        assertEquals("actionTime", dao.getOrderByClause());
    }
    
}
