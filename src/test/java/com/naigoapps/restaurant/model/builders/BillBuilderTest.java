/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Order;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class BillBuilderTest {

    private BillBuilder builder;

    @Before
    public void setUp() {
        builder = new BillBuilder();
    }
    
    @Test
    public void testProgressive() {
        Integer progressive = 4;
        Bill result = builder.progressive(progressive).getContent();
        assertEquals(progressive, result.getProgressive());
    }

    @Test
    public void testTable() {
        DiningTable table = new DiningTableBuilder().getContent();
        Bill result = builder.table(table).getContent();
        assertEquals(table, result.getTable());
        assertTrue(table.getBills().contains(result));
    }

    @Test
    public void testOrder() {
        Order order = new OrderBuilder().getContent();
        Bill bill = builder.order(order).getContent();
        assertEquals(bill, order.getBill());
        assertTrue(bill.getOrders().contains(order));
    }

    @Test
    public void testOrders() {
        Order order1 = new OrderBuilder().getContent();
        Bill bill = builder.orders(Arrays.asList(order1)).getContent();
        
        assertEquals(bill, order1.getBill());
        assertTrue(bill.getOrders().contains(order1));
    }

    @Test
    public void testOrdersTwice() {
        Order order1 = new OrderBuilder().getContent();
        Order order2 = new OrderBuilder().getContent();
        Order order3 = new OrderBuilder().getContent();
        Bill bill = builder
                .orders(Arrays.asList(order1))
                .orders(Arrays.asList(order2, order3)).getContent();
        
        assertEquals(bill, order1.getBill());
        assertEquals(bill, order2.getBill());
        assertEquals(bill, order3.getBill());
        assertTrue(bill.getOrders().contains(order1));
        assertTrue(bill.getOrders().contains(order2));
        assertTrue(bill.getOrders().contains(order3));
    }

}
