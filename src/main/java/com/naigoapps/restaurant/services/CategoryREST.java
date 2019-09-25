/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.mappers.CategoryMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class CategoryREST {

    @Inject
    private CategoryDao dao;
    
    @Inject
    private DishDao dDao;
    
    @Inject
    private LocationDao lDao;

    @Inject
    private AdditionDao aDao;
    
    @Inject
    private CategoryMapper mapper;

    public void setEntityManager(EntityManager em){
        dao.setEntityManager(em);
        dDao.setEntityManager(em);
        lDao.setEntityManager(em);
        aDao.setEntityManager(em);
    }
    
    @GET
    public List<CategoryDTO> findAll() {
        return dao.findAll()
        		.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GET
    @Path("{uuid}")
    public CategoryDTO find(@PathParam("uuid") String uuid) {
    	return mapper.map(dao.findByUuid(uuid));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCategory() {
        Category category = new Category();        
        dao.persist(category);
        return category.getUuid();
    }

    @PUT
    @Path("{uuid}/name")
    public CategoryDTO updateCategoryName(@PathParam("uuid") String uuid, String name) {
        return setCategoryProperty(cat -> cat.setName(name), uuid);
    }

    @PUT
    @Path("{uuid}/location")
    public CategoryDTO updateCategoryLocation(@PathParam("uuid") String uuid, String locUuid) {
        Location loc = lDao.findByUuid(locUuid);
        if (loc != null) {
            return setCategoryProperty(cat -> cat.setLocation(loc), uuid);
        }else {
        	throw new BadRequestException("Postazione non trovata");
        }
    }

    @PUT
    @Path("{uuid}/additions")
    public CategoryDTO updateCategoryAdditions(@PathParam("uuid") String uuid, String[] additionUuids) {
        additionUuids = additionUuids == null ? new String[0] : additionUuids;
        List<Addition> additions = Arrays.stream(additionUuids)
                .map(addition -> aDao.findByUuid(addition))
                .collect(Collectors.toList());
        if (additions.stream().allMatch(Objects::nonNull)) {
            return setCategoryProperty(cat -> cat.setAdditions(additions), uuid);
        } else {
            throw new BadRequestException("Impossibile trovare alcune varianti");
        }
    }

    @PUT
    @Path("{uuid}/color")
	public CategoryDTO updateCategoryColor(@PathParam("uuid") String uuid, String color) {
		return setCategoryProperty(cat -> cat.setColor(color), uuid);
	}

    private CategoryDTO setCategoryProperty(Consumer<Category> setter, String uuid) {
        Category cat = dao.findByUuid(uuid);
        if (cat != null) {
            setter.accept(cat);
            return mapper.map(cat);
        } else {
            throw new BadRequestException("Categoria non trovata");
        }
    }

    @DELETE
    @Path("{uuid}")
    @Produces(MediaType.TEXT_PLAIN)
    public void deleteCategory(@PathParam("uuid") String uuid) {
        dao.deleteByUuid(uuid);
    }

}
