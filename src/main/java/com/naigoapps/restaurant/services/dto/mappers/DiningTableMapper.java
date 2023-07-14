/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.DiningTableExportDTO;
import com.naigoapps.restaurant.services.dto.DiningTableSkeletonDTO;

/**
 *
 * @author naigo
 */
@Mapper(componentModel = "spring", config = MapperConfiguration.class, uses = { BillMapper.class, WaiterMapper.class, PhaseMapper.class,
		RestaurantTableMapper.class, OrdinationMapper.class })
public abstract class DiningTableMapper {

	@Mapping(target = "orders", source = "orders")
	@Mapping(target = "coverCharge", source = "evening.coverCharge")
	@Mapping(target = "eveningId", source = "evening.uuid")
	@Mapping(target = "total", source = "totalPrice")
	public abstract DiningTableDTO map(DiningTable table);

	@Mapping(target = "coverCharge", source = "evening.coverCharge")
	@Mapping(target = "eveningId", source = "evening.uuid")
	@Mapping(target = "total", source = "totalPrice")
	public abstract DiningTableSkeletonDTO mapSkeleton(DiningTable table);

	@AfterMapping
	public void afterMapping(@MappingTarget DiningTableDTO dto) {
		dto.getOrdinations().sort((o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()));
	}
}
