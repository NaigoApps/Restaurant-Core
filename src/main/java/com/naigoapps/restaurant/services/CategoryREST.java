/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.builders.CategoryBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    private CategoryDao categoryDao;
    
    @Inject
    private DishDao dishDao;
    
    @Inject
    private LocationDao lDao;

    @Inject
    private AdditionDao aDao;

    public void setEntityManager(EntityManager em){
        categoryDao.setEntityManager(em);
        dishDao.setEntityManager(em);
        lDao.setEntityManager(em);
        aDao.setEntityManager(em);
    }
    
    @GET
    @Transactional
    public Response findAll() {
        List<CategoryDTO> categories = categoryDao.findAll().stream()
                .map(category -> DTOAssembler.fromCategory(category))
                .collect(Collectors.toList());

        return Response
                .status(200)
                .entity(categories)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCategory(CategoryDTO newCategory) {
        Category category = new CategoryBuilder().
                name(newCategory.getName()).
                color(new Color(newCategory.getColor() != null ? newCategory.getColor() : 0)).
                location(lDao.findByUuid(newCategory.getLocation()))
                .getContent();
        
        if(newCategory.getDishes() != null){
            category.setDishes(newCategory.getDishes().stream()
                .map(dishUuid -> dishDao.findByUuid(dishUuid))
                .collect(Collectors.toList()));
        }
        
        if(newCategory.getAdditions() != null){
            category.setAdditions(newCategory.getAdditions().stream()
                .map(additionUuid -> aDao.findByUuid(additionUuid))
                .collect(Collectors.toList()));
        }

        categoryDao.persist(category);

        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromCategory(category))
                .build();
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateCategoryName(@PathParam("uuid") String uuid, String name) {
        return setCategoryProperty(cat -> cat.setName(name), uuid);
    }

    @PUT
    @Path("{uuid}/location")
    @Transactional
    public Response updateCategoryLocation(@PathParam("uuid") String uuid, String locUuid) {
        Location loc = lDao.findByUuid(locUuid);
        if (loc != null) {
            return setCategoryProperty(cat -> cat.setLocation(loc), uuid);
        }
        return ResponseBuilder.notFound("Postazione non trovata");
    }

    @PUT
    @Path("{uuid}/additions")
    @Transactional
    public Response updateCategoryAdditions(@PathParam("uuid") String uuid, String[] additionUuids) {
        additionUuids = additionUuids == null ? new String[0] : additionUuids;
        List<Addition> additions = Arrays.stream(additionUuids)
                .map(addition -> aDao.findByUuid(addition))
                .collect(Collectors.toList());
        if (additions.stream().allMatch(a -> a != null)) {
            return setCategoryProperty(cat -> cat.setAdditions(additions), uuid);
        } else {
            return ResponseBuilder.badRequest("Impossibile trovare alcune varianti");
        }
    }

    @PUT
    @Path("{uuid}/color")
    @Transactional
    public Response updateCategoryColor(@PathParam("uuid") String uuid, Integer rgb) {
        if (rgb != null) {
            return setCategoryProperty(cat -> cat.setColor(new Color(rgb)), uuid);
        }
        return ResponseBuilder.badRequest("Received invalid color: " + rgb);
    }

    private Response setCategoryProperty(Consumer<Category> setter, String uuid) {
        Category cat = categoryDao.findByUuid(uuid);
        if (cat != null) {
            setter.accept(cat);
            return ResponseBuilder.ok(DTOAssembler.fromCategory(cat));
        } else {
            return ResponseBuilder.notFound("Categoria non trovata");
        }
    }

    @DELETE
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCategory(String uuid) {
        categoryDao.deleteByUuid(uuid);
        return ResponseBuilder.ok(uuid);
    }

}
