/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.utils;

import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
public class ResponseBuilder {
    
    public static Response ok(){
        return ok(null);
    }
    
    public static Response ok(Object body){
        return Response
                .ok()
                .entity(body)
                .build();
    }
    
    public static Response created(Object body){
        return Response
                .ok()
                .entity(body)
                .build();
    }
    
    public static Response notFound(Object body){
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(body)
                .build();
    }
    
    public static Response badRequest(Object body){
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(body)
                .build();
    }
}
