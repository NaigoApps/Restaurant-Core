/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import com.naigoapps.restaurant.model.builders.OrderBuilder;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class OrdinationTest {

    private Ordination ordination;
    
    @Before
    public void setUp() {
        ordination = new Ordination();
    }

    @Test
    public void testSetDirty() {
        ordination.setDirty(false);
        assertFalse(ordination.isDirty());
    }

    @Test
    public void testSetProgressive() {
        ordination.setProgressive(555);
        assertEquals(555, ordination.getProgressive());
    }

    @Test
    public void testSetOrders() {
        assertNotNull(ordination.getOrders());
        Order o = new Order();
        ordination.setOrders(Arrays.asList(o));
        assertEquals(1, ordination.getOrders().size());
        assertTrue(ordination.getOrders().contains(o));
        assertEquals(ordination, o.getOrdination());
    }

    @Test
    public void testAddOrders() {
        Order o = new Order();
        ordination.addOrder(o);
        assertEquals(1, ordination.getOrders().size());
        assertTrue(ordination.getOrders().contains(o));
        assertEquals(ordination, o.getOrdination());
    }
    
    @Test
    public void testClearOrders() {
        Order o = new OrderBuilder().getContent();
        ordination.addOrder(o);
        assertEquals(1, ordination.getOrders().size());
        ordination.clearOrders();
        assertEquals(0, ordination.getOrders().size());
        assertNull(o.getOrdination());
    }

    @Test
    public void testSetCreationTime() {
        LocalDateTime now = LocalDateTime.now();
        ordination.setCreationTime(now);
        assertEquals(now, ordination.getCreationTime());
    }

    @Test
    public void testSetTable() {
        DiningTable table = new DiningTable();
        ordination.setTable(table);
        assertEquals(table, ordination.getTable());
        assertTrue(table.getOrdinations().contains(ordination));
    }
    
    @Test
    public void testSetTableNull() {
        DiningTable table = new DiningTable();
        ordination.setTable(table);
        ordination.setTable(null);
        assertNull(ordination.getTable());
        assertFalse(table.getOrdinations().contains(ordination));
    }
    
}
