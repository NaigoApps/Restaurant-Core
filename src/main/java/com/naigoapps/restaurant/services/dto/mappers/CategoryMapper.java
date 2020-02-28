/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.DishDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Comparator;

/**
 *
 * @author naigo
 */
@Mapper(componentModel = "spring", config = MapperConfiguration.class, uses = { LocationMapper.class, DishMapper.class, AdditionMapper.class })
public abstract class CategoryMapper {

	public abstract CategoryDTO map(Category c);

	@AfterMapping
	public void order(@MappingTarget CategoryDTO dto){
		dto.getDishes().sort(Comparator.comparing(DishDTO::getName));
	}

}
