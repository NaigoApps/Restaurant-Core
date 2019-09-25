/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;

import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.services.dto.LocationDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { PrinterMapper.class })
public interface LocationMapper {

	LocationDTO map(Location l);

}
