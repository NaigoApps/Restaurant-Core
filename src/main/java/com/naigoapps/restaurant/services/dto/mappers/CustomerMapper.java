/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;

import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.services.dto.CustomerDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class)
public interface CustomerMapper {

	CustomerDTO map(Customer c);

}
