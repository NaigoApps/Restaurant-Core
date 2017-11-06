/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author naigo
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper>{

    private final ObjectMapper MAPPER;

    public ObjectMapperContextResolver() {
        this.MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
    
    
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return MAPPER;
    }
    
}
