/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class BillTest {

    private Bill bill;
    
    @Before
    public void setUp() {
        bill = new Bill();
    }

    @Test
    public void testSetOrders() {
        assertNotNull(bill.getOrders());
        
        Order order = new Order();
        
        bill.setOrders(new ArrayList<>(Arrays.asList(order)));
        
        assertEquals(1, bill.getOrders().size());
        assertTrue(bill.getOrders().contains(order));
        assertEquals(bill, order.getBill());
    }
    
    @Test
    public void testAddOrder() {
        Order order = new Order();
        
        bill.addOrder(order);
        
        assertEquals(1, bill.getOrders().size());
        assertTrue(bill.getOrders().contains(order));
        assertEquals(bill, order.getBill());
    }

    @Test
    public void testSetTable(){
        DiningTable table = new DiningTable();
        bill.setTable(table);
        assertEquals(table, bill.getTable());
        assertTrue(table.getBills().contains(bill));
    }
    
}
