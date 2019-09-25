/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.EveningDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = {DiningTableMapper.class})
public abstract class EveningMapper {

    public abstract EveningDTO map(Evening evening);
    
	@AfterMapping
	public void afterMapping(@MappingTarget EveningDTO dto) {
		dto.getDiningTables().sort(DiningTableDTO.comparator().reversed());
	}
    
}
