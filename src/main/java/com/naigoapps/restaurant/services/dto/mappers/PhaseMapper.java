/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.services.dto.PhaseDTO;

import org.mapstruct.Mapper;

/**
 *
 * @author naigo
 */
@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface PhaseMapper {

    public PhaseDTO map(Phase table);

}
