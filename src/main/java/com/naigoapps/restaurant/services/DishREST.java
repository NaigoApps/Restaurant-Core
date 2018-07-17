/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.DishStatus;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.services.dto.DishDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
@Path("/dishes")
@Produces(MediaType.APPLICATION_JSON)
public class DishREST {

    @Inject
    private DishDao dishDao;

    @Inject
    private OrderDao oDao;

    @Inject
    private CategoryDao categoryDao;

    @GET
    public Response findByCategory(@QueryParam("category") String catUuid) {
        List<DishDTO> dishes = dishDao.findByCategory(catUuid).stream()
                .filter(d -> DishStatus.REMOVED != d.getStatus())
                .map(DTOAssembler::fromDish)
                .collect(Collectors.toList());

        return Response
                .status(200)
                .entity(dishes)
                .build();
    }

    @GET
    @Path("all")
    public Response findAll() {
        List<DishDTO> dishes = dishDao.findAll().stream()
                .filter(d -> DishStatus.REMOVED != d.getStatus())
                .map(DTOAssembler::fromDish)
                .collect(Collectors.toList());

        return Response
                .status(200)
                .entity(dishes)
                .build();
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateName(@PathParam("uuid") String uuid, String name) {
        Dish d = dishDao.findByUuid(uuid);
        if (d != null) {
            d.setName(name);

            return Response
                    .status(Response.Status.OK)
                    .entity(DTOAssembler.fromDish(d))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("{uuid}/description")
    @Transactional
    public Response updateDescription(@PathParam("uuid") String uuid, String description) {
        Dish d = dishDao.findByUuid(uuid);
        if (d != null) {
            d.setDescription(description);

            return Response
                    .status(Response.Status.OK)
                    .entity(DTOAssembler.fromDish(d))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("{uuid}/price")
    @Transactional
    public Response updatePrice(@PathParam("uuid") String uuid, float price) {
        Dish d = dishDao.findByUuid(uuid);
        if (d != null) {
            d.setPrice(price);

            return Response
                    .status(Response.Status.OK)
                    .entity(DTOAssembler.fromDish(d))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("{uuid}/status")
    @Transactional
    public Response updateStatus(@PathParam("uuid") String uuid, String status) {
        Dish d = dishDao.findByUuid(uuid);
        if (d != null) {
            if (!DishStatus.REMOVED.equals(DishStatus.fromName(status)) || !hasOrders(d)) {

                d.setStatus(DishStatus.fromName(status));

                return ResponseBuilder.ok(DTOAssembler.fromDish(d));
            }
            return ResponseBuilder.badRequest("Il piatto è usato in alcuni ordini");
        } else {
            return ResponseBuilder.notFound("Piatto non trovato");
        }
    }

    private boolean hasOrders(Dish d) {
        return oDao.countByDish(d) > 0;
    }

    @PUT
    @Path("{uuid}/category")
    @Transactional
    public Response updateCategory(@PathParam("uuid") String dishUuid, String catUuid) {
        Dish d = dishDao.findByUuid(dishUuid);
        Category cat = categoryDao.findByUuid(catUuid);
        if (d != null) {
            d.setCategory(cat);

            return Response
                    .status(Response.Status.OK)
                    .entity(DTOAssembler.fromDish(d))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Transactional
    public Response createDish(DishDTO newDish) {

        Category cat = categoryDao.findByUuid(newDish.getCategory());

        if (cat != null) {
            Dish dish = new DishBuilder()
                    .category(cat)
                    .name(newDish.getName())
                    .description(newDish.getDescription())
                    .price(newDish.getPrice())
                    .getContent();
            dishDao.persist(dish);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(DTOAssembler.fromDish(dish))
                    .build();
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .build();
    }

    @DELETE
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteDish(String uuid) {
        dishDao.deleteByUuid(uuid);
        return ResponseBuilder.ok(uuid);
    }
}
