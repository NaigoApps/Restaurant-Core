/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.mobile;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.services.dto.mobile.CategoryDTO;
import com.naigoapps.restaurant.services.dto.mobile.mappers.CategoryMapper;

/**
 *
 * @author naigo
 */
@Path("/mobile/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryREST {

	@Inject
	private CategoryDao categoryDao;

	@Inject
	private CategoryMapper mapper;

	@GET
	@Transactional
	public List<CategoryDTO> findAll() {
		return categoryDao.findAll()
				.stream()
				.map(mapper::map)
				.collect(Collectors.toList());
	}

}
