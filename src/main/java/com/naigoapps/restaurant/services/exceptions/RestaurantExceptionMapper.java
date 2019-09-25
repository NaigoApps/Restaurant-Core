package com.naigoapps.restaurant.services.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.naigoapps.restaurant.services.utils.ResponseBuilder;

@Provider
public class RestaurantExceptionMapper implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable exception) {
		exception.printStackTrace();
		return ResponseBuilder.badRequest(exception.getMessage());
	}

}
