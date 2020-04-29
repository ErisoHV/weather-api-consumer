package com.weather.exception;

import org.springframework.http.ResponseEntity;


public class WeatherServiceException extends RuntimeException {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2611853092939168775L;

	public WeatherServiceException(String msg){
		super(msg);
	}
	
	public static <T> String buildErrorResponse (ResponseEntity<T> response) {
		if (response == null)	
			return "Response is null";
		if (response.getBody() == null)
			return "Response body is null";
		else
			return response.getBody().toString();
	}
}
