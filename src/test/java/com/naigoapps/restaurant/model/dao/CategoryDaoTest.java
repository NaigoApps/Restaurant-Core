/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Category;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author naigo
 */
public class CategoryDaoTest{

    private CategoryDao dao;
    
    @Before
    public void setUp(){
        dao = new CategoryDao();
    }
    
    @Test
    public void testGetOrderByClause() {
        assertEquals("name", dao.getOrderByClause());
    }
    
    public void testGetEntityClass(){
        assertEquals(Category.class, dao.getEntityClass());
    }

}
