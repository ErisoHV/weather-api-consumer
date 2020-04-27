package com.weather.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, 
	reason="Invalid configuration parameter")
public class WeatherIlegalParameterException extends IllegalArgumentException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5695713392468760537L;

	public WeatherIlegalParameterException(String parameter, List<String> validValues) {
		super("Invalid request parameter: " + parameter + ". Value must be: "
				+ String.join(", ", validValues));
	}
	
}
