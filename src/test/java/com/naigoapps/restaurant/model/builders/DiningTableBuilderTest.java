/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class DiningTableBuilderTest {

    private DiningTableBuilder builder;
    
    @Before
    public void setUp() {
        builder = new DiningTableBuilder();
    }

    @Test
    public void testWaiter() {
        Waiter waiter = new WaiterBuilder().getContent();
        assertEquals(waiter, builder.waiter(waiter).getContent().getWaiter());
    }

    @Test
    public void testTable() {
        RestaurantTable table = new RestaurantTableBuilder().getContent();
        assertEquals(table, builder.table(table).getContent().getTable());
    }

    @Test
    public void testCcs() {
        assertEquals(5, builder.ccs(5).getContent().getCoverCharges());
    }

    @Test
    public void testDate() {
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now, builder.date(now).getContent().getDate());
    }

    @Test
    public void testEvening() {
        Evening evening = new EveningBuilder().getContent();
        DiningTable result = builder.evening(evening).getContent();
        assertEquals(evening, result.getEvening());
        assertTrue(evening.getDiningTables().contains(result));
    }

    @Test
    public void testClosed() {
        assertTrue(builder.closed(true).getContent().isClosed());
    }

    @Test
    public void testBill() {
        Bill bill = new BillBuilder().getContent();
        DiningTable result = builder.bill(bill).getContent();
        assertEquals(result, bill.getTable());
        assertTrue(result.getBills().contains(bill));
    }

    @Test
    public void testOrdination() {
        Ordination ordination = new OrdinationBuilder().getContent();
        DiningTable result = builder.ordination(ordination).getContent();
        assertEquals(result, ordination.getTable());
        assertTrue(result.getOrdinations().contains(ordination));
    }

}
