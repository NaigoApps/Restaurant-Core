/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.DishStatus;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.services.dto.DishDTO;
import com.naigoapps.restaurant.services.dto.mappers.DishMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/dishes")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class DishREST {

    @Inject
    private DishDao dao;

    @Inject
    private OrderDao oDao;

    @Inject
    private CategoryDao categoryDao;
    
    @Inject
    private DishMapper mapper;
    
    @GET
    @Path("{uuid}")
    public DishDTO findByUuid(@PathParam("uuid") String uuid) {
    	return mapper.map(dao.findByUuid(uuid));
    }
    
    @GET
    public List<DishDTO> findByCategory(@QueryParam("category") String catUuid) {
        return dao.findByCategory(catUuid).stream()
                .filter(d -> DishStatus.SUSPENDED != d.getStatus())
                .filter(d -> DishStatus.REMOVED != d.getStatus())
                .sorted((d1, d2) -> d1.getName().compareTo(d2.getName()))
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @GET
    @Path("all")
    public List<DishDTO> findAll() {
        return dao.findAll().stream()
                .filter(d -> DishStatus.REMOVED != d.getStatus())
                .sorted((d1, d2) -> d1.getName().compareTo(d2.getName()))
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("{uuid}/name")
    public DishDTO updateName(@PathParam("uuid") String uuid, String name) {
    	return updateDishProperty(uuid, d -> d.setName(name));
    }

    @PUT
    @Path("{uuid}/description")
    public DishDTO updateDescription(@PathParam("uuid") String uuid, String description) throws Exception {
    	return updateDishProperty(uuid, d -> d.setDescription(description));
    }

    @PUT
    @Path("{uuid}/price")
    public DishDTO updatePrice(@PathParam("uuid") String uuid, float price) {
    	return updateDishProperty(uuid, d -> d.setPrice(price));
    }

    @PUT
    @Path("{uuid}/status")
    public DishDTO updateStatus(@PathParam("uuid") String uuid, String statusText) {
    	DishStatus status = DishStatus.valueOf(statusText);
    	return updateDishProperty(uuid, d -> {    		
    		if (!DishStatus.REMOVED.equals(status) || !hasOrders(d)) {
    			d.setStatus(status);
    		}else {
    			throw new BadRequestException("Il piatto è usato in alcuni ordini");
    		}
    	});
    }
    
    private DishDTO updateDishProperty(String dishUuid, Consumer<Dish> consumer) {
    	Dish dish = dao.findByUuid(dishUuid);
    	if(dish == null) {
    		throw new BadRequestException("Piatto non trovato");
    	}
    	consumer.accept(dish);
    	return mapper.map(dish);
    }

    private boolean hasOrders(Dish d) {
        return oDao.countByDish(d) > 0;
    }

    @PUT
    @Path("{uuid}/category")
    public DishDTO updateCategory(@PathParam("uuid") String dishUuid, String catUuid) {
    	Category cat = categoryDao.findByUuid(catUuid);
        return updateDishProperty(dishUuid, d -> d.setCategory(cat));
    }

    @POST
    public String createDish(@QueryParam("category") String category) {
        Category cat = categoryDao.findByUuid(category);

        if (cat != null) {
            Dish dish = new DishBuilder()
                    .category(cat)
                    .getContent();
            dao.persist(dish);

            return dish.getUuid();
        }else {        	
        	throw new BadRequestException("Categoria non valida");
        }
    }

    @DELETE
    @Path("{uuid}")
    @Produces(MediaType.TEXT_PLAIN)
    public void deleteDish(@PathParam("uuid") String uuid) {
        dao.deleteByUuid(uuid);
    }
}
