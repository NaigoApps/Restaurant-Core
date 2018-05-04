/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.builders.CustomerBuilder;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.services.dto.CustomerDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
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
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerREST {

    @Inject
    CustomerDao cDao;

    @GET
    public Response getCustomers() {

        List<CustomerDTO> data = cDao.findAll().stream()
                .map(DTOAssembler::fromCustomer)
                .collect(Collectors.toList());

        return ResponseBuilder.ok(data);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCustomer(CustomerDTO c) {
        Customer newCustomer = new CustomerBuilder()
                .name(c.getName())
                .surname(c.getSurname())
                .cf(c.getCf())
                .piva(c.getPiva())
                .address(c.getAddress())
                .city(c.getCity())
                .cap(c.getCap())
                .district(c.getDistrict())
                .getContent();
        cDao.persist(newCustomer);

        return ResponseBuilder.created(DTOAssembler.fromCustomer(newCustomer));
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public Response updateCustomerName(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setName(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/surname")
    @Transactional
    public Response updateCustomerSurname(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setSurname(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/cf")
    @Transactional
    public Response updateCustomerCf(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setCf(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/piva")
    @Transactional
    public Response updateCustomerPiva(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setPiva(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/city")
    @Transactional
    public Response updateCustomerCity(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setCity(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/cap")
    @Transactional
    public Response updateCustomerCap(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setCap(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/address")
    @Transactional
    public Response updateCustomerAddress(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setAddress(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @PUT
    @Path("{uuid}/district")
    @Transactional
    public Response updateCustomerDistrict(@PathParam("uuid") String uuid, String value) {
        Customer c = cDao.findByUuid(uuid, Customer.class);
        if (c != null) {
            c.setDistrict(value);
            return ResponseBuilder.ok(DTOAssembler.fromCustomer(c));
        } else {
            return ResponseBuilder.notFound("Cliente non trovato");
        }
    }

    @DELETE
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCustomer(String uuid) {
        cDao.getEntityManager().remove(cDao.findByUuid(uuid, Customer.class));
        return ResponseBuilder.ok(uuid);
    }

}
