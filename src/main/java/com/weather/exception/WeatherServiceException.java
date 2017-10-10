package com.weather.exception;

import org.springframework.web.client.RestClientException;

public class WeatherServiceException extends RestClientException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2611853092939168775L;

	public WeatherServiceException(String msg) {
		super(msg);
	}
}
