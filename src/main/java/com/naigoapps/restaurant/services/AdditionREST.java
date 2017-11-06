/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.builders.AdditionBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.services.dto.AdditionDTO;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/additions")
@Produces(MediaType.APPLICATION_JSON)
public class AdditionREST {
    
    @Inject
    AdditionDao aDao;
    
    @GET
    public Response getAllAdditions() {
        List<AdditionDTO> additions = aDao.findAll().stream()
                .map(DTOAssembler::fromAddition)
                .collect(Collectors.toList());
        
        
        return Response
                .status(200)
                .entity(additions)
                .build();
    }
    
    @POST
    @Transactional
    public Response createAddition(AdditionDTO a){
        Addition addition = new AdditionBuilder()
                .name(a.getName())
                .price(a.getPrice())
                .generic(a.isGeneric())
                .getContent();
        aDao.persist(addition);
        
        return Response
                .status(Response.Status.CREATED)
                .entity(DTOAssembler.fromAddition(addition))
                .build();
    }
    
    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateAdditionName(@PathParam("uuid") String uuid, String name){
        Addition a = aDao.findByUuid(uuid);
        a.setName(name);
        return Response.status(200).entity(DTOAssembler.fromAddition(a)).build();
    }
    
    @PUT
    @Path("{uuid}/price")
    @Transactional
    public Response updateWaiterSurname(@PathParam("uuid") String uuid, float price){
        Addition a = aDao.findByUuid(uuid);
        a.setPrice(price);
        return Response.status(200).entity(DTOAssembler.fromAddition(a)).build();
    }
    
    @PUT
    @Path("{uuid}/generic")
    @Transactional
    public Response updateAdditionGeneric(@PathParam("uuid") String uuid, boolean generic){
        Addition a = aDao.findByUuid(uuid);
        a.setGeneric(generic);
        return Response.status(200).entity(DTOAssembler.fromAddition(a)).build();
    }
        
    @DELETE
    @Transactional
    public Response deleteAddition(String uuid){
        
        aDao.removeByUuid(uuid);
        
        return Response
                .status(Response.Status.OK)
                .build();
    }
}
