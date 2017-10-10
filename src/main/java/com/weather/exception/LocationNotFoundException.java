package com.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Location not found in the weather service")
public class LocationNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8747463169847302471L;

}
