/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Location;

/**
 *
 * @author naigo
 */
public class CategoryBuilderTest {

    private CategoryBuilder builder;
    
    @Before
    public void setUp() {
        builder = new CategoryBuilder();
    }

    @Test
    public void testName() {
        assertEquals("name", builder.name("name").getContent().getName());
    }

    @Test
    public void testLocation() {
        Location location = new LocationBuilder().getContent();
        assertEquals(location, builder.location(location).getContent().getLocation());
    }

    @Test
    public void testDish() {
        Dish dish = new DishBuilder().getContent();
        Category result = builder.dish(dish).getContent();
        assertEquals(result, dish.getCategory());
        assertTrue(result.getDishes().contains(dish));
    }

    @Test
    public void testAddition() {
        Addition addition = new AdditionBuilder().getContent();
        assertTrue(builder.addition(addition).getContent().getAdditions().contains(addition));
    }
    
    @Test
    public void testColor(){
        Category cat = builder.color("black").getContent();
        assertEquals("black", cat.getColor());
    }

    @Test
    public void testQuick() {
        Location loc = new LocationBuilder().getContent();
        Category cat = builder.quick("A", loc).getContent();
        assertEquals("A", cat.getName());
        assertEquals(loc, cat.getLocation());
    }

    @Test
    public void testGetContent() {
    }

}
