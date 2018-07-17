/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.builders.CategoryBuilder;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.utils.WeldTestRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author naigo
 */
@RunWith(WeldTestRunner.class)
public class DaoTest extends AbstractPersistenceTest{
    
    @Override
    public void insertData() {
        Category cat = new CategoryBuilder().getContent();
        Dish dish = new DishBuilder().category(cat).getContent();
        getEntityManager().persist(dish);
        getEntityManager().persist(cat);
    }
    
    @Test
    public void testCategory(){
        assertEquals(1, categoryDao.findAll().size());
    }
    
    @Test
    public void testDish(){
        assertEquals(1, dishDao.findAll().size());
    }
    
    @Test
    public void testLink(){
        Dish d = dishDao.findAll().get(0);
        Category c = categoryDao.findAll().get(0);
        assertEquals(c, d.getCategory());
    }
    
}
