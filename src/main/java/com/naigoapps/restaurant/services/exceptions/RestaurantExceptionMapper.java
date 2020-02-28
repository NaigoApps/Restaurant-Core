package com.naigoapps.restaurant.services.exceptions;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestaurantExceptionMapper {

	@ResponseBody
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<?> handleException(Throwable ex) {
		LoggerFactory.getLogger(getClass()).error("Error", ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

}
