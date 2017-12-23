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
public class InvoiceTest {

    private Invoice invoice;
    
    @Before
    public void setUp() {
        invoice = new Invoice();
    }

    @Test
    public void testSetCustomer() {
        Customer c = new Customer();
        invoice.setCustomer(c);
        assertEquals(c, invoice.getCustomer());
    }
    
}
