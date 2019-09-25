/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;

import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.services.dto.OrderDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { DishMapper.class, PhaseMapper.class, AdditionMapper.class })
public interface OrderMapper {

	public OrderDTO map(Order o);

}
