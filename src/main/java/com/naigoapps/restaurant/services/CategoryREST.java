/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.DishStatus;
import com.naigoapps.restaurant.model.builders.CategoryBuilder;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryREST {
    
    @Inject
    CategoryDao categoryDao;
    
    @Inject
    LocationDao lDao;
    
    @GET
    public Response findAll() {
        List<CategoryDTO> categories = categoryDao.findAll().stream()
                .map(category -> DTOAssembler.fromCategory(category))
                .collect(Collectors.toList());
        
        return Response
                .status(200)
                .entity(categories)
                .build();
    }
    
    @GET
    @Path("menu")
    public Response findMenu() {
        List<CategoryDTO> categories = categoryDao.findAll().stream()
                .map(category -> DTOAssembler.fromCategory(category))
                .collect(Collectors.toList());
        
        categories.forEach(category -> {
            category.setDishes(category.getDishes().stream()
                    .filter(dish -> dish.getStatus() == DishStatus.ACTIVE)
                    .collect(Collectors.toList())
            );
        });
        
        return Response
                .status(200)
                .entity(categories)
                .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCategory(CategoryDTO newCategory){
        Category category = new CategoryBuilder().
                name(newCategory.getName())
                .getContent();
        
        categoryDao.persist(category);
        
        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromCategory(category))
                .build();
    }
    
    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateCategoryName(@PathParam("uuid") String uuid, String name){
        Category c = categoryDao.findByUuid(uuid);
        c.setName(name);
        return Response.status(200).entity(DTOAssembler.fromCategory(c)).build();
    }
    
    @PUT
    @Path("{uuid}/location")
    @Transactional
    public Response updateCategoryLocation(@PathParam("uuid") String uuid, String location){
        Category c = categoryDao.findByUuid(uuid);
        c.setLocation(lDao.findByUuid(location));
        return Response.status(200).entity(DTOAssembler.fromCategory(c)).build();
    }
        
    @DELETE
    @Transactional
    public Response deleteCategory(String uuid){
        
        categoryDao.removeByUuid(uuid);
        
        return Response
                .status(200)
                .build();
    }

}
