/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.DishStatus;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class DishBuilderTest {
    
    private DishBuilder builder;
    
    @Before
    public void setUp() {
        builder = new DishBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testDescription() {
        assertEquals("desc", builder.description("desc").getContent().getDescription());
    }

    @Test
    public void testPrice() {
        assertEquals(6.0f, builder.price(6.0f).getContent().getPrice(), 0.0f);
    }

    @Test
    public void testCategory() {
        Category cat = new CategoryBuilder().getContent();
        Dish result = builder.category(cat).getContent();
        assertEquals(cat, result.getCategory());
        assertTrue(cat.getDishes().contains(result));
    }

    @Test
    public void testDefaultStatus() {
        assertEquals(DishStatus.ACTIVE, builder.getContent().getStatus());
    }

    @Test
    public void testStatus() {
        assertEquals(DishStatus.SUSPENDED, builder.status(DishStatus.SUSPENDED).getContent().getStatus());
    }

}
