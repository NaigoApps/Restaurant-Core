/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.services.dto.CategoryDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { LocationMapper.class, DishMapper.class, AdditionMapper.class })
public interface CategoryMapper {

	CategoryDTO map(Category c);

}
