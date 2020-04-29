package com.weather.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CurrentWeatherStatus {
	private Location location;
	private Timestamp epochTime;
	private String weatherDescription;
	private String weatherIcon;
	private double temperature;
	private double realFeelTemperature;
	private String windDirection;
	private double windSpeed;
	private double precipitation;
	private String weatherServiceName;
		
}
