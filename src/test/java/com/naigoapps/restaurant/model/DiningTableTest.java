/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class DiningTableTest {

    private DiningTable table;

    @Before
    public void setUp() {
        table = new DiningTableBuilder().getContent();
    }

    @Test
    public void testSetWaiter() {
        Waiter w = new Waiter();
        table.setWaiter(w);
        assertEquals(w, table.getWaiter());
    }

    @Test
    public void testListOrders() {
        Order o1 = new Order();
        Order o2 = new Order();
        Order o3 = new Order();
        Ordination or1 = new Ordination();
        or1.setOrders(Arrays.asList(o1, o2));
        Ordination or2 = new Ordination();
        or2.setOrders(Arrays.asList(o3));
        table.setOrdinations(Arrays.asList(or1, or2));

        assertEquals(3, table.getOrders().size());
        assertTrue(table.getOrders().contains(o1));
        assertTrue(table.getOrders().contains(o2));
        assertTrue(table.getOrders().contains(o3));
    }

    @Test
    public void testSetOrdinations() {
        assertNotNull(table.getOrdinations());

        Ordination or1 = new Ordination();
        table.setOrdinations(Arrays.asList(or1));

        assertEquals(1, table.getOrdinations().size());
        assertTrue(table.getOrdinations().contains(or1));
        assertEquals(table, or1.getTable());
    }

    @Test
    public void testAddOrdinations() {
        Ordination or1 = new Ordination();
        table.addOrdination(or1);

        assertEquals(1, table.getOrdinations().size());
        assertTrue(table.getOrdinations().contains(or1));
        assertEquals(table, or1.getTable());
    }

    @Test
    public void testRemoveOrdination() {
        Ordination or1 = new Ordination();
        table.addOrdination(or1);
        table.removeOrdination(or1);
        assertFalse(table.getOrdinations().contains(or1));
        assertNull(or1.getTable());
    }

    @Test
    public void testSetCoverCharges() {
        table.setCoverCharges(5);
        assertEquals(5, table.getCoverCharges());
    }

    @Test
    public void testSetDate() {
        LocalDateTime now = LocalDateTime.now();
        table.setOpeningTime(now);
        assertEquals(now, table.getOpeningTime());
    }

    @Test
    public void testSetTable() {
        RestaurantTable t = new RestaurantTable();
        table.setTable(t);
        assertEquals(t, table.getTable());
    }

    @Test
    public void testSetEvening() {
        Evening e = new Evening();
        table.setEvening(e);
        assertEquals(e, table.getEvening());
        assertTrue(e.getDiningTables().contains(table));
    }

    @Test
    public void testSetEveningNull() {
        Evening e = new Evening();
        table.setEvening(e);
        table.setEvening(null);
        assertNull(table.getEvening());
        assertFalse(e.getDiningTables().contains(table));
    }

    @Test
    public void testSetStatus() {
        assertEquals(DiningTableStatus.OPEN, table.getStatus());
        table.setStatus(DiningTableStatus.CLOSED);
        assertEquals(DiningTableStatus.CLOSED, table.getStatus());
    }

    @Test
    public void testGetTotalPriceCC() {
        Evening e = new Evening();
        e.setCoverCharge(1.0f);
        table.setEvening(e);
        table.setCoverCharges(4);
        assertEquals(4.0f, table.getTotalPrice(), 0.0f);
    }

    @Test
    public void testGetTotalPrice() {
        table.setEvening(new Evening());
        Order o1 = new Order();
        o1.setPrice(1.0f);
        Order o2 = new Order();
        o2.setPrice(2.0f);
        Order o3 = new Order();
        o3.setPrice(3.0f);
        Ordination or1 = new Ordination();
        or1.setOrders(Arrays.asList(o1, o2));
        Ordination or2 = new Ordination();
        or2.setOrders(Arrays.asList(o3));

        table.setOrdinations(Arrays.asList(or1, or2));
        assertEquals(6.0f, table.getTotalPrice(), 0.0f);
    }

    @Test
    public void testSetBills() {
        assertNotNull(table.getBills());
        Bill b = new BillBuilder().getContent();
        table.setBills(Arrays.asList(b));
        assertEquals(1, table.getBills().size());
        assertTrue(table.getBills().contains(b));
        assertEquals(table, b.getTable());

    }

    @Test
    public void testAddBill() {
        Bill b = new BillBuilder().getContent();
        table.addBill(b);
        assertEquals(1, table.getBills().size());
        assertTrue(table.getBills().contains(b));
        assertEquals(table, b.getTable());
    }

}
