/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.services.dto.DishDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author naigo
 */
@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface DishMapper {
    
	@Mapping(target = "color", source="category.color")
	@Mapping(target = "categoryId", source="category.uuid")
    DishDTO map(Dish d);
    
}
