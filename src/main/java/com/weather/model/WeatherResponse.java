package com.weather.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class WeatherResponse {
	private CurrentWeatherStatus weather;
	private String service;
	private String message;
	private HttpStatus status;
}
