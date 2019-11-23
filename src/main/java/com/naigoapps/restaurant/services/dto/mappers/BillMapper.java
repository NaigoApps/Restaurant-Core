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

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.BillExportDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { CustomerMapper.class, OrdinationMapper.class, OrderMapper.class })
public interface BillMapper {

	@Mapping(source = "estimatedTotal", target = "estimatedTotal")
	@Mapping(source = "table.uuid", target = "tableUuid")
	public BillDTO map(Bill b);

	public BillExportDTO mapForExport(Bill b);

	default List<String> map(List<Order> orders){
		return orders.stream()
				.map(Order::getUuid)
				.collect(Collectors.toList());
	}

}
