/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.services.dto.mobile.CategoryDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { DishMapper.class, AdditionMapper.class })
public interface CategoryMapper {

	@Mapping(target = "color", ignore = true)
	CategoryDTO map(Category c);

}
