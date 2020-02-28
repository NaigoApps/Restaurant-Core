/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;

/**
 *
 * @author naigo
 */
@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public abstract class RestaurantTableMapper {

	@Autowired
	private EveningManager eveningManager;

	@Mapping(target = "busy", ignore = true)
	public abstract RestaurantTableDTO map(RestaurantTable d);

	@AfterMapping
	public void setIsBusy(@MappingTarget RestaurantTableDTO dto) {
		Evening e = eveningManager.getSelectedEvening();
		if (e != null) {
			dto.setBusy(e.getDiningTables().stream().filter(dt -> dt.getStatus() != DiningTableStatus.CLOSED)
					.filter(dt -> dt.getTable() != null && dt.getTable().getUuid().equals(dto.getUuid())).count() > 0);
		}
	}
}
