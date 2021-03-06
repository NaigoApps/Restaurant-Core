/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author naigo
 */
public class CategoryTest {

    private Category category;
    
    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void testSetName() {
        category.setName("name");
        assertEquals("name", category.getName());
    }

    @Test
    public void testSetDishes() {
        assertNotNull(category.getDishes());
        Dish d = new Dish();
        category.setDishes(Arrays.asList(d));
        assertEquals(1, category.getDishes().size());
        assertTrue(category.getDishes().contains(d));
        assertEquals(category, d.getCategory());   
    }
    
    @Test
    public void testAddDish(){
        Dish d = new Dish();
        category.addDish(d);
        assertEquals(1, category.getDishes().size());
        assertTrue(category.getDishes().contains(d));
        assertEquals(category, d.getCategory());   
    }

    @Test
    public void testSetLocation() {
        Location l = new Location();
        category.setLocation(l);
        assertEquals(l, category.getLocation());
    }
    
    @Test
    public void testSetAdditions(){
        assertNotNull(category.getAdditions());
        Addition a = new Addition();
        category.setAdditions(Arrays.asList(a));
        assertEquals(1, category.getAdditions().size());
        assertTrue(category.getAdditions().contains(a));
    }

    @Test
    public void testSetColor() {
        category.setColor("A");
        assertEquals("A", category.getColor());
    }
}
