/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class OrderTest {
    
    private Order order;
    
    private Order o1;
    private Order o2;
    
    @Before
    public void setUp() {
        order = new Order();
        o1 = new Order();
        o2 = new Order();
    }

    @Test
    public void testSetBill() {
        Bill b = new Bill();
        order.setBill(b);
        assertEquals(b, order.getBill());
        assertTrue(b.getOrders().contains(order));
    }

    @Test
    public void testSetPhase() {
        Phase p = new Phase();
        order.setPhase(p);
        assertEquals(p, order.getPhase());
    }

    @Test
    public void testSetDish() {
        Dish d = new Dish();
        order.setDish(d);
        assertEquals(d, order.getDish());
    }

    @Test
    public void testSetPrice() {
        order.setPrice(5.0f);
        assertEquals(5.0f, order.getPrice(), 0.0f);
    }

    @Test
    public void testSetNotes() {
        order.setNotes("...");
        assertEquals("...", order.getNotes());
    }

    @Test
    public void testSetAdditions() {
        assertNotNull(order.getAdditions());
        Addition a = new Addition();
        order.setAdditions(Arrays.asList(a));
        assertEquals(1, order.getAdditions().size());
        assertTrue(order.getAdditions().contains(a));
    }
    
    @Test
    public void testClearAdditions(){
        Addition a = new Addition();
        order.setAdditions(Arrays.asList(a));
        order.clearAdditions();
        assertTrue(order.getAdditions().isEmpty());
    }

    @Test
    public void testSetOrdination() {
        Ordination o = new Ordination();
        order.setOrdination(o);
        assertEquals(o, order.getOrdination());
        assertTrue(o.getOrders().contains(order));
    }

    @Test
    public void testIsTheSame() {
        assertTrue(o1.isTheSame(o2));
    }

    @Test
    public void testIsTheSameDish() {
        Dish d1 = new Dish();
        Dish d2 = new Dish();
        o1.setDish(d1);
        o2.setDish(d1);
        assertTrue(o1.isTheSame(o2));
        o2.setDish(d2);
        assertFalse(o1.isTheSame(o2));
    }

    @Test
    public void testIsTheSameNotes() {
        o1.setNotes("a");
        o2.setNotes("b");
        assertFalse(o1.isTheSame(o2));
    }

    @Test
    public void testIsTheSameAdditions() {
        Addition a1 = new Addition();
        Addition a2 = new Addition();
        o1.setAdditions(Arrays.asList(a1));
        o2.setAdditions(Arrays.asList(a1));
        assertTrue(o1.isTheSame(o2));
        
        o2.setAdditions(Arrays.asList(a1, a2));
        assertFalse(o1.isTheSame(o2));
        
    }
    
}
