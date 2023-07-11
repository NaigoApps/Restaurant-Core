/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

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
			boolean busy = e.getDiningTables().stream()
					.filter(RestaurantTableMapper::isBusy)
					.anyMatch(dt -> dt.getTable() != null && dt.getTable().getUuid().equals(dto.getUuid()));
			dto.setBusy(busy);
		}
	}

	private static boolean isBusy(DiningTable dt) {
		return dt.getStatus() != DiningTableStatus.CLOSED && dt.getStatus() != DiningTableStatus.ARCHIVED;
	}
}
