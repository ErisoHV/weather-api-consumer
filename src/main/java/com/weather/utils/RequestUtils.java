package com.weather.utils;

import com.weather.exception.WeatherIlegalParameterException;
import com.weather.model.WeatherRequest;

public class RequestUtils {

	private RequestUtils() {
		// Empty Constructor
	}

	public static boolean isOKWeatherKeyRequest(WeatherRequest request) {
		return request.getKey() != null && !request.getKey().isEmpty();
	}

	public static boolean isOKLocationRequest(WeatherRequest request){
			return request.getLocation() != null && request.getLocation().getLatitude() != null
					&& request.getLocation().getLongitude() != null;
	}

	public static boolean isOKNameLocationRequest(WeatherRequest request) {
		return request.getLocation() != null  && request.getLocation().getName() != null
				&& !request.getLocation().getName().isEmpty();
	}

	public static boolean isOKLanguageRequest(WeatherRequest request){
		return request.getLanguage() != null && !request.getLanguage().toString().isEmpty();
	}
	
	public static String getCommaSeparatedLocationFromRequest(WeatherRequest request) 
			throws WeatherIlegalParameterException{
		if (isOKLocationRequest(request)) {
			return request.getLocation().getLatitude() 
					+ "," + request.getLocation().getLongitude();
		} else {
			throw new WeatherIlegalParameterException("location", "not null");
		}
	}
	
}
