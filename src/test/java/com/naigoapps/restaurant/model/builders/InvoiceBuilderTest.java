/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Customer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class InvoiceBuilderTest {
    
    private InvoiceBuilder builder;
    
    @Before
    public void setUp() {
        builder = new InvoiceBuilder();
    }

    @Test
    public void testCustomer() {
        Customer customer = new CustomerBuilder().getContent();
        assertEquals(customer, builder.customer(customer).getContent().getCustomer());
    }

}
