/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class CustomerBuilderTest {
    
    private CustomerBuilder builder;
    
    @Before
    public void setUp() {
        builder = new CustomerBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testSurname() {
        assertEquals("surname", builder.surname("surname").getContent().getSurname());
    }

    @Test
    public void testCity() {
        assertEquals("city", builder.city("city").getContent().getCity());
    }

    @Test
    public void testCap() {
        assertEquals("cap", builder.cap("cap").getContent().getCap());
    }

    @Test
    public void testDistrict() {
        assertEquals("dis", builder.district("dis").getContent().getDistrict());
    }

    @Test
    public void testPiva() {
        assertEquals("pi", builder.piva("pi").getContent().getPiva());
    }

    @Test
    public void testCf() {
        assertEquals("cf", builder.cf("cf").getContent().getCf());
    }

    @Test
    public void testAddress() {
        assertEquals("a", builder.address("a").getContent().getAddress());
    }

}
