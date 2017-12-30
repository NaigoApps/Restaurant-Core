/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class OrderBuilderTest {
    
    private OrderBuilder builder;
    
    @Before
    public void setUp() {
        builder = new OrderBuilder();
    }

    @Test
    public void testDish() {
        Dish dish = new DishBuilder().getContent();
        Order result = builder.dish(dish).getContent();
        assertEquals(dish, result.getDish());
    }

    @Test
    public void testOrdination() {
        Ordination ordination = new OrdinationBuilder().getContent();
        Order result = builder.ordination(ordination).getContent();
        assertEquals(ordination, result.getOrdination());
        assertTrue(ordination.getOrders().contains(result));
    }

    @Test
    public void testPrice() {
        assertEquals(3.0f, builder.price(3.0f).getContent().getPrice(), 0.0f);
    }

    @Test
    public void testNotes() {
        assertEquals("...", builder.notes("...").getContent().getNotes());
    }

    @Test
    public void testPhase() {
        Phase phase = new PhaseBuilder().getContent();
        assertEquals(phase, builder.phase(phase).getContent().getPhase());
    }

    @Test
    public void testAddition() {
        Addition addition = new AdditionBuilder().getContent();
        assertTrue(builder.addition(addition).getContent().getAdditions().contains(addition));
    }

}
