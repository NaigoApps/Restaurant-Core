package com.naigoapps.restaurant.services.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.naigoapps.restaurant.services.utils.ResponseBuilder;

@Provider
public class RestaurantExceptionMapper implements ExceptionMapper<Throwable>{

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public Response toResponse(Throwable exception) {
		logger.log(Level.WARNING, "Error", exception);
		return ResponseBuilder.badRequest(exception.getMessage());
	}

}
