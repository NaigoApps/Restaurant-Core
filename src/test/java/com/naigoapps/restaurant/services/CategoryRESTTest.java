/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.builders.AdditionBuilder;
import com.naigoapps.restaurant.model.builders.CategoryBuilder;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.model.builders.LocationBuilder;
import com.naigoapps.restaurant.model.dao.AbstractPersistenceTest;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class CategoryRESTTest extends AbstractPersistenceTest{

    private CategoryBuilder builder;
    
    private String locationUuid;
    private String dishUuid;
    private String additionUuid;
    
    @Inject
    private CategoryREST service;

    public CategoryRESTTest() {
        builder = new CategoryBuilder();
    }

    @Override
    public void setUp() {
        super.setUp();
        service.setEntityManager(getEntityManager());
    }
    
    
    
    @Override
    public void insertData() {
        Location l = new LocationBuilder().name("L").getContent();
        Dish d1 = new DishBuilder().name("D1").getContent();
        Dish d2 = new DishBuilder().name("D2").getContent();
        Dish d3 = new DishBuilder().name("D3").getContent();
        Addition a = new AdditionBuilder().name("A").getContent();
        
        locationUuid = l.getUuid();
        dishUuid = d3.getUuid();
        additionUuid = a.getUuid();
        
        categoryDao.persist(l, d1, d2, d3, a);
        categoryDao.persist(
                builder.quick("C1", l).dish(d1).addition(a).getContent(),
                builder.quick("C2", l).dish(d2).getContent()
        );
    }
    
    

    @Test
    public void testFindAll() {
        Response r = service.findAll();
        assertEquals(200, r.getStatus());
        
        List<Category> cats = (List<Category>) r.getEntity();
        assertEquals(2, cats.size());
    }

    @Test
    public void testCreateCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("C3");
        dto.setLocation(locationUuid);
        dto.setDishes(Arrays.asList(dishUuid));
        //TODO COLOR
        dto.setAdditions(Arrays.asList(additionUuid));
        
        Response r = service.createCategory(dto);
        
        assertEquals(201, r.getStatus());
        
        CategoryDTO result = (CategoryDTO) r.getEntity();
        assertEquals("C3", result.getName());
        assertEquals(locationUuid, result.getLocation());
        assertEquals(1, result.getDishes().size());
        assertEquals(dishUuid, result.getDishes().get(0));
        assertEquals(1, result.getAdditions().size());
        assertEquals(additionUuid, result.getAdditions().get(0));
    }

    @Test
    public void testUpdateCategoryName() {
    }

    @Test
    public void testUpdateCategoryLocation() {
    }

    @Test
    public void testUpdateCategoryAdditions() {
    }

    @Test
    public void testUpdateCategoryColor() {
    }

    @Test
    public void testDeleteCategory() {
    }
    
}
