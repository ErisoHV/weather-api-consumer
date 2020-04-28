package com.weather.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;


public class WeatherServiceException extends RuntimeException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2611853092939168775L;

	public <T> WeatherServiceException(ResponseEntity<T> response) {
		if (response == null)
			throw new RestClientException("Response is null");
		if (response.getBody() == null)
			throw new RestClientException("Response body is null");
		else
			throw new RestClientException(response.getBody().toString());
	}
	
	public WeatherServiceException(String msg){
		super(msg);
	}
}
