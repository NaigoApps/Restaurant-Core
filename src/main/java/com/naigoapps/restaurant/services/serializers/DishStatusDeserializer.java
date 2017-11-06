/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.naigoapps.restaurant.model.DishStatus;
import java.io.IOException;

/**
 *
 * @author naigo
 */
public class DishStatusDeserializer extends JsonDeserializer<DishStatus>{

    @Override
    public DishStatus deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        return DishStatus.fromName(jp.getText());
    }
    
}
