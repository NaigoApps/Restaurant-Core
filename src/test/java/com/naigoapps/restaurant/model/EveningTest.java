/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class EveningTest {
    
    private Evening evening;
    
    @Before
    public void setUp() {
        evening = new Evening();
    }

    @Test
    public void testSetDay() {
        LocalDate now = LocalDate.now();
        evening.setDay(now);
        assertEquals(now, evening.getDay());
    }

    @Test
    public void testSetDiningTables() {
        assertNotNull(evening.getDiningTables());
        
        DiningTable table = new DiningTable();
        evening.setDiningTables(Arrays.asList(table));
        
        assertEquals(1, evening.getDiningTables().size());
        assertTrue(evening.getDiningTables().contains(table));
        assertEquals(evening, table.getEvening());
    }

    @Test
    public void testAddDiningTable(){
        DiningTable table = new DiningTable();
        evening.addDiningTable(table);
    
        assertEquals(1, evening.getDiningTables().size());
        assertTrue(evening.getDiningTables().contains(table));
        assertEquals(evening, table.getEvening());
    }
    
    @Test
    public void testSetCoverCharge() {
        evening.setCoverCharge(5.0f);
        assertEquals(5.0f, evening.getCoverCharge(), 0.0f);
    }

}
