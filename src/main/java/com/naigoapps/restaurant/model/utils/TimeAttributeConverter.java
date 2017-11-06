/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author naigo
 */
@Converter(autoApply = true)
public class TimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDate) {
    	return (locDate == null ? null : Timestamp.valueOf(locDate));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlDate) {
    	return (sqlDate == null ? null : sqlDate.toLocalDateTime());
    }
}