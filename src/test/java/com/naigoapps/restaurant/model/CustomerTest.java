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
public class CustomerTest {
    
    private Customer customer;

    @Before
    public void setUp() {
        customer = new Customer();
    }

    @Test
    public void testSetName() {
        customer.setName("name");
        assertEquals("name", customer.getName());
    }

    @Test
    public void testSetSurname() {
        customer.setSurname("surname");
        assertEquals("surname", customer.getSurname());
    }

    @Test
    public void testSetPiva() {
        customer.setPiva("piva");
        assertEquals("piva", customer.getPiva());
    }

    @Test
    public void testSetCf() {
        customer.setCf("cf");
        assertEquals("cf", customer.getCf());
    }

    @Test
    public void testSetAddress() {
        customer.setAddress("addr");
        assertEquals("addr", customer.getAddress());
    }

    @Test
    public void testSetCity() {
        customer.setCity("city");
        assertEquals("city", customer.getCity());
    }

    @Test
    public void testSetCap() {
        customer.setCap("cap");
        assertEquals("cap", customer.getCap());
    }

    @Test
    public void testSetDistrict() {
        customer.setDistrict("dis");
        assertEquals("dis", customer.getDistrict());
    }
    
}
