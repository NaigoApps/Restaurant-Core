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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.builders.CustomerBuilder;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.services.dto.CustomerDTO;
import com.naigoapps.restaurant.services.dto.mappers.CustomerMapper;

/**
 *
 * @author naigo
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerREST {

    @Inject
    private CustomerDao dao;

    @Inject
    private CustomerMapper mapper;
    
    @GET
    public List<CustomerDTO> getCustomers() {
        return dao.findAll().stream()
        		.map(mapper::map)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO c) {
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
        dao.persist(newCustomer);

        return mapper.map(newCustomer);
    }

    @PUT
    @Path("{uuid}/name")
    @Transactional
    public CustomerDTO updateCustomerName(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setName(value), uuid);
    }

    @PUT
    @Path("{uuid}/surname")
    @Transactional
    public CustomerDTO updateCustomerSurname(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setSurname(value), uuid);
    }

    @PUT
    @Path("{uuid}/cf")
    @Transactional
    public CustomerDTO updateCustomerCf(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setCf(value), uuid);
    }

    @PUT
    @Path("{uuid}/piva")
    @Transactional
    public CustomerDTO updateCustomerPiva(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setPiva(value), uuid);
    }

    @PUT
    @Path("{uuid}/city")
    @Transactional
    public CustomerDTO updateCustomerCity(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setCity(value), uuid);
    }

    @PUT
    @Path("{uuid}/cap")
    @Transactional
    public CustomerDTO updateCustomerCap(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setCap(value), uuid);
    }

    @PUT
    @Path("{uuid}/address")
    @Transactional
    public CustomerDTO updateCustomerAddress(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setAddress(value), uuid);
    }

    @PUT
    @Path("{uuid}/district")
    @Transactional
    public CustomerDTO updateCustomerDistrict(@PathParam("uuid") String uuid, String value) {
        return updateCustomerProperty(customer -> customer.setDistrict(value), uuid);
    }
    
    public CustomerDTO updateCustomerProperty(Consumer<Customer> setter, String uuid){
        Customer c = dao.findByUuid(uuid);
        if (c != null) {
            setter.accept(c);
            return mapper.map(c);
        } else {
            throw new BadRequestException("Cliente non trovato");
        }
    }

    @DELETE
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public void deleteCustomer(String uuid) {
        dao.getEntityManager().remove(dao.findByUuid(uuid));
    }
}
