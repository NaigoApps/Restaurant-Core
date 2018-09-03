/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.utils;

import java.awt.Color;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author naigo
 */
@Converter(autoApply = true)
public class ColorAttributeConverter implements AttributeConverter<Color, Integer>{

    @Override
    public Integer convertToDatabaseColumn(Color attribute) {
        return attribute != null ? attribute.getRGB() : 0;
    }

    @Override
    public Color convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new Color(dbData, true) : new Color(0);
    }
    
}
