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
import com.naigoapps.restaurant.model.WaiterStatus;
import java.io.IOException;

/**
 *
 * @author naigo
 */
public class WaiterStatusDeserializer extends JsonDeserializer<WaiterStatus>{

    @Override
    public WaiterStatus deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        return WaiterStatus.fromName(jp.getText());
    }
    
}
