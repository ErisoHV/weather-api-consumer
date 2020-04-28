package com.weather.services.core.interfaces;

import java.util.List;
import java.util.Map;

import com.weather.model.Location;
import com.weather.model.WeatherRequest;

public interface LocationData {
	
	public List<Location> getLocationsDataByName(WeatherRequest request);
	
	public Location getLocationDataByGeoposition(WeatherRequest request);
	
	public Location responseToLocation(Map<String, Object> element);
	
}
