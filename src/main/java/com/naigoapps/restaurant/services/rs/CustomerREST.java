/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.builders.CustomerBuilder;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.services.dto.CustomerDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author naigo
 */
@RequestMapping("/rest/customers")
@RestController
@Transactional
public class CustomerREST {

    @Autowired
    private CustomerDao dao;

    @Autowired
    private CustomerMapper mapper;

    @GetMapping("{uuid}")
    public CustomerDTO getCustomer(@PathVariable("uuid") String uuid) {
        return mapper.map(dao.findByUuid(uuid));
    }

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return dao.findAll().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PostMapping
    public String createCustomer() {
        Customer newCustomer = new CustomerBuilder().getContent();
        dao.persist(newCustomer);

        return newCustomer.getUuid();
    }

    @PutMapping("{uuid}/name")
    public CustomerDTO updateCustomerName(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setName(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/surname")
    public CustomerDTO updateCustomerSurname(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setSurname(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/cf")
    public CustomerDTO updateCustomerCf(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setCf(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/piva")
    public CustomerDTO updateCustomerPiva(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setPiva(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/city")
    public CustomerDTO updateCustomerCity(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setCity(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/cap")
    public CustomerDTO updateCustomerCap(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setCap(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/address")
    public CustomerDTO updateCustomerAddress(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setAddress(value.getValue()), uuid);
    }

    @PutMapping("{uuid}/district")
    public CustomerDTO updateCustomerDistrict(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> value) {
        return updateCustomerProperty(customer -> customer.setDistrict(value.getValue()), uuid);
    }

    public CustomerDTO updateCustomerProperty(Consumer<Customer> setter, String uuid) {
        Customer c = dao.findByUuid(uuid);
        if (c != null) {
            setter.accept(c);
            return mapper.map(c);
        } else {
            throw new RuntimeException("Cliente non trovato");
        }
    }

    @DeleteMapping("{uuid}")
    public void deleteCustomer(@PathVariable("uuid") String uuid) {
        dao.getEntityManager().remove(dao.findByUuid(uuid));
    }
}
