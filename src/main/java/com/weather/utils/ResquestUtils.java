package com.weather.utils;

import com.weather.exception.WeatherIlegalParameterException;
import com.weather.model.WeatherRequest;

public class ResquestUtils {

	public static boolean isOKWeatherRequest(WeatherRequest request) {
		return request.getKey() != null && !request.getKey().isEmpty();
	}
	
	public static String getCommaSeparatedLocationFromRequest(WeatherRequest request) 
			throws WeatherIlegalParameterException{
		if (request.getLocation() != null) {
			return request.getLocation().getLatitude() 
					+ "," + request.getLocation().getLongitude();
		} else {
			throw new WeatherIlegalParameterException("location", "not null");
		}
	}
	
}
