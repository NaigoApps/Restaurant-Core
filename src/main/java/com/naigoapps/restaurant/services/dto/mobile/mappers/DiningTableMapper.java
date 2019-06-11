/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.services.dto.mobile.DiningTableDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = {WaiterMapper.class, PhaseMapper.class, RestaurantTableMapper.class, OrdinationMapper.class})
public interface DiningTableMapper {

	@Mapping(target = "eveningId", source = "evening.uuid")
    DiningTableDTO map(DiningTable table);

}
