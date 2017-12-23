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
public class DishTest {
    
    private Dish dish;
    
    @Before
    public void setUp() {
        dish = new Dish();
    }

    @Test
    public void testSetStatus() {
        dish.setStatus(DishStatus.REMOVED);
        assertEquals(DishStatus.REMOVED, dish.getStatus());
    }

    @Test
    public void testSetCategory() {
        Category cat = new Category();
        dish.setCategory(cat);
        assertEquals(cat, dish.getCategory());
        assertTrue(cat.getDishes().contains(dish));
    }

    @Test
    public void testSetName() {
        dish.setName("name");
        assertEquals("name", dish.getName());
    }

    @Test
    public void testSetPrice() {
        dish.setPrice(5.0f);
        assertEquals(5.0f, dish.getPrice(), 0.0f);
    }

    @Test
    public void testSetDescription() {
        dish.setDescription("desc");
        assertEquals("desc", dish.getDescription());
    }
    
}
