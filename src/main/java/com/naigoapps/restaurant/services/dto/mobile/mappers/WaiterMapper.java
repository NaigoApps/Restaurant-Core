/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mobile.mappers;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.services.dto.mobile.WaiterDTO;
import org.mapstruct.Mapper;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class)
public interface WaiterMapper {
    
    WaiterDTO map(Waiter d);
    
}
