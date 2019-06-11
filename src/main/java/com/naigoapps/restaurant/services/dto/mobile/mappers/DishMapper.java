/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile.mappers;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.services.dto.mobile.DishDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class)
public interface DishMapper {
    
	@Mapping(target = "categoryId", source="category.uuid")
    DishDTO map(Dish d);
    
}
