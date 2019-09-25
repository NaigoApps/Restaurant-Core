/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.services.dto.BillDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { CustomerMapper.class, OrdinationMapper.class })
public interface BillMapper {

	@Mapping(source = "estimatedTotal", target = "estimatedTotal")
	@Mapping(source = "table.uuid", target = "tableUuid")
	public BillDTO map(Bill b);

}
