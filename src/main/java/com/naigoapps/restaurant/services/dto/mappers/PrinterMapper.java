/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.services.dto.PrinterDTO;

/**
 *
 * @author naigo
 */
@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface PrinterMapper {

	PrinterDTO map(Printer c);

}
