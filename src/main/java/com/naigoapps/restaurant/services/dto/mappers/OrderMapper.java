/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.OrderExportDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { DishMapper.class, PhaseMapper.class, AdditionMapper.class })
public interface OrderMapper {

	public OrderDTO map(Order o);

	@Mapping(target = "dish", source = "dish.uuid")
	@Mapping(target = "phase", source = "phase.uuid")
	public OrderExportDTO mapForExport(Order o);

	default List<String> map(List<Addition> additions){
		return additions.stream()
				.map(Addition::getUuid)
				.collect(Collectors.toList());
	}
}
