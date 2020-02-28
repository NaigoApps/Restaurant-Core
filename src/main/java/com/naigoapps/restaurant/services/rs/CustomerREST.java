/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.builders.CustomerBuilder;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.services.dto.CustomerDTO;
import com.naigoapps.restaurant.services.dto.mappers.CustomerMapper;

/**
 *
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

	@GetMapping
	public List<CustomerDTO> getCustomers() {
		return dao.findAll().stream().map(mapper::map).collect(Collectors.toList());
	}

	@PostMapping
	public CustomerDTO createCustomer(@RequestBody CustomerDTO c) {
		Customer newCustomer = new CustomerBuilder().name(c.getName()).surname(c.getSurname()).cf(c.getCf())
				.piva(c.getPiva()).address(c.getAddress()).city(c.getCity()).cap(c.getCap()).district(c.getDistrict())
				.getContent();
		dao.persist(newCustomer);

		return mapper.map(newCustomer);
	}

	@PutMapping("{uuid}/name")
	public CustomerDTO updateCustomerName(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setName(value), uuid);
	}

	@PutMapping("{uuid}/surname")
	public CustomerDTO updateCustomerSurname(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setSurname(value), uuid);
	}

	@PutMapping("{uuid}/cf")
	public CustomerDTO updateCustomerCf(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setCf(value), uuid);
	}

	@PutMapping("{uuid}/piva")
	public CustomerDTO updateCustomerPiva(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setPiva(value), uuid);
	}

	@PutMapping("{uuid}/city")
	public CustomerDTO updateCustomerCity(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setCity(value), uuid);
	}

	@PutMapping("{uuid}/cap")
	public CustomerDTO updateCustomerCap(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setCap(value), uuid);
	}

	@PutMapping("{uuid}/address")
	public CustomerDTO updateCustomerAddress(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setAddress(value), uuid);
	}

	@PutMapping("{uuid}/district")
	public CustomerDTO updateCustomerDistrict(@PathVariable("uuid") String uuid, @RequestBody String value) {
		return updateCustomerProperty(customer -> customer.setDistrict(value), uuid);
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

	@DeleteMapping
	public void deleteCustomer(String uuid) {
		dao.getEntityManager().remove(dao.findByUuid(uuid));
	}
}
