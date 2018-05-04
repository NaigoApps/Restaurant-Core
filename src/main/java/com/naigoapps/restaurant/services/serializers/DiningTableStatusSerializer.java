/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.naigoapps.restaurant.model.DiningTableStatus;
import java.io.IOException;

/**
 *
 * @author naigo
 */
public class DiningTableStatusSerializer extends JsonSerializer<DiningTableStatus>{

    @Override
    public void serialize(DiningTableStatus t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeString(t.getName());
    }
    
}
