/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.services.dto.OrdersGroupDTO;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.OrdinationExportDTO;
import com.naigoapps.restaurant.services.dto.PhaseOrdersDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { OrderMapper.class })
public abstract class OrdinationMapper {

	@Inject
	private PhaseMapper pMapper;

	@Inject
	private AdditionMapper aMapper;

	@Inject
	private DishMapper dMapper;

	@Inject
	private OrderMapper oMapper;

	public abstract OrdinationExportDTO mapForExport(Ordination a);

	@Mapping(target = "diningTableId", source = "table.uuid")
	@Mapping(source = "orders", target = "orders", qualifiedByName = "map")
	public abstract OrdinationDTO map(Ordination a);

	protected List<PhaseOrdersDTO> map(List<Order> orders) {
		List<PhaseOrdersDTO> result = new ArrayList<>();
		orders.stream().collect(Collectors.groupingBy(Order::getPhase)).forEach((phase, phaseOrders) -> {
			PhaseOrdersDTO po = new PhaseOrdersDTO();
			po.setPhase(pMapper.map(phase));
			po.setOrders(group(phaseOrders));
			result.add(po);
		});
		return result;
	}

	public List<OrdersGroupDTO> group(List<Order> orders) {
		List<OrdersGroupDTO> result = new ArrayList<>();
		for (Order order : orders) {
			boolean found = false;
			for (OrdersGroupDTO group : result) {
				if (!found && group.matches(order)) {
					group.setQuantity(group.getQuantity() + 1);
					group.getOrders().add(oMapper.map(order));
					found = true;
				}
			}
			if (!found) {
				OrdersGroupDTO groupDto = new OrdersGroupDTO();
				groupDto.setPhaseName(order.getPhase().getName());
				groupDto.setDish(dMapper.map(order.getDish()));
				groupDto.setAdditions(order.getAdditions().stream().map(aMapper::map).collect(Collectors.toList()));
				groupDto.setNotes(order.getNotes());
				groupDto.setPrice(order.getPrice());
				groupDto.setQuantity(1);
				groupDto.getOrders().add(oMapper.map(order));
				result.add(groupDto);
			}
		}
		return result;
	}

}
