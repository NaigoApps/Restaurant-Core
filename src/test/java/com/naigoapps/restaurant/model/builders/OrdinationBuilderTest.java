/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class OrdinationBuilderTest {
    
    private OrdinationBuilder builder;
    
    @Before
    public void setUp() {
        builder = new OrdinationBuilder();
    }

    @Test
    public void testProgressive() {
        assertEquals(4, builder.progressive(4).getContent().getProgressive());
    }

    @Test
    public void testCreationTime() {
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now, builder.creationTime(now).getContent().getCreationTime());
    }

    @Test
    public void testTable() {
        DiningTable table = new DiningTableBuilder().getContent();
        Ordination result = builder.table(table).getContent();
        assertEquals(table, result.getTable());
        assertTrue(table.getOrdinations().contains(result));
    }

    @Test
    public void testDirty() {
        assertTrue(builder.dirty(true).getContent().isDirty());
    }

    @Test
    public void testOrder() {
        Order order = new OrderBuilder().getContent();
        Ordination result = builder.order(order).getContent();
        assertTrue(result.getOrders().contains(order));
        assertTrue(result.equals(order.getOrdination()));
    }

}
