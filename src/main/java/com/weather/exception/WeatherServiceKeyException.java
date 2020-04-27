package com.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, 
	reason="The Service Key cannot be null, missing key")
public class WeatherServiceKeyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WeatherServiceKeyException() {
		super("The Service Key cannot be null, missing key");
	}
}
