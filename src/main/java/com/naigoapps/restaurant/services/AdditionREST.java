/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.builders.AdditionBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.services.dto.AdditionDTO;
import com.naigoapps.restaurant.services.dto.mappers.AdditionMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/additions")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class AdditionREST {
    
    @Inject
    private AdditionDao dao;
    
    @Inject
    private DishDao dDao;
    
    @Inject
    private AdditionMapper mapper;

    @GET
    public List<AdditionDTO> find(@QueryParam("dish") String dishUuid, @QueryParam("generic") Boolean generic) {
    	Set<Addition> result = new HashSet<>();
    	if(dishUuid != null) {
	    	Dish dish = dDao.findByUuid(dishUuid);
	        result.addAll(dish.getCategory().getAdditions());
	        result.addAll(dao.findGeneric());
    	}else if(generic != null){
    		result.addAll(dao.findWhere("generic=" + generic));
    	}else {
    		result.addAll(dao.findAll());
    	}
    	return result.stream()
    			.sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
    			.map(mapper::map)
    			.collect(Collectors.toList());
    }
    
    @GET
    @Path("{uuid}")
    public AdditionDTO find(@PathParam("uuid") String uuid) {
    	return mapper.map(dao.findByUuid(uuid));
    }
    
    @POST
    public String createAddition(){
        Addition addition = new AdditionBuilder().name("").getContent();
        dao.persist(addition);
        return addition.getUuid();
    }
    
    @PUT
    @Path("{uuid}/name")
    public AdditionDTO updateAdditionName(@PathParam("uuid") String uuid, String name){
        Addition a = dao.findByUuid(uuid);
        a.setName(name);
        return mapper.map(a);
    }
    
    @PUT
    @Path("{uuid}/price")
    public AdditionDTO updateWaiterSurname(@PathParam("uuid") String uuid, float price){
        Addition a = dao.findByUuid(uuid);
        a.setPrice(price);
        return mapper.map(a);
    }
    
    @PUT
    @Path("{uuid}/generic")
    @Consumes(MediaType.APPLICATION_JSON)
    public AdditionDTO updateAdditionGeneric(@PathParam("uuid") String uuid, String generic){
        Addition a = dao.findByUuid(uuid);
        a.setGeneric(Boolean.valueOf(generic));
        return mapper.map(a);
    }
        
    @DELETE
    @Path("{uuid}")
    @Produces(MediaType.TEXT_PLAIN)
    public void deleteAddition(@PathParam("uuid") String uuid){
        dao.deleteByUuid(uuid);
    }
}
