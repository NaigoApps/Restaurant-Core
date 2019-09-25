/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.mappers;

import org.mapstruct.Mapper;

import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.services.dto.SettingsDTO;

/**
 *
 * @author naigo
 */
@Mapper(config = MapperConfiguration.class, uses = { PrinterMapper.class })
public interface SettingsMapper {

	SettingsDTO map(Settings c);

}
