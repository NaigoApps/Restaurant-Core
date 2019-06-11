/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.services.dto.mobile.OrdersGroupDTO;
import com.naigoapps.restaurant.services.dto.mobile.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.mobile.PhaseOrdersDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class)
public abstract class OrdinationMapper {
    
    @Inject
    private PhaseMapper pMapper;
    
    @Inject
    private AdditionMapper aMapper;
    
    @Inject
    private DishMapper dMapper;

    @Mapping(source = "orders", target = "orders", qualifiedByName = "map")
    public abstract OrdinationDTO map(Ordination a);

    protected List<PhaseOrdersDTO> map(List<Order> orders) {
        List<PhaseOrdersDTO> result = new ArrayList<>();
        orders.stream()
                .collect(Collectors.groupingBy(o -> o.getPhase()))
                .forEach((phase, phaseOrders) -> {
                    PhaseOrdersDTO po = new PhaseOrdersDTO();
                    po.setPhase(pMapper.map(phase));
                    po.setOrders(group(phaseOrders));
                    result.add(po);
                });
        return result;
    }
    
    protected List<OrdersGroupDTO> group(List<Order> orders){
        List<OrdersGroupDTO> result = new ArrayList<>();
        orders.stream()
                .collect(Collectors.groupingBy(o -> new OrderKind(o)))
                .forEach((oc, group) -> {
                    OrdersGroupDTO groupDto = new OrdersGroupDTO();
                    groupDto.setDish(dMapper.map(oc.getOrderDish()));
                    groupDto.setAdditions(oc.getOrderAdditions().stream()
                        .map(aMapper::map).collect(Collectors.toList()));
                    groupDto.setNotes(oc.getOrderNotes());
                    groupDto.setPrice(oc.getOrderPrice());
                    groupDto.setQuantity(group.size());
                    result.add(groupDto);
                });
        return result;
    }

}
