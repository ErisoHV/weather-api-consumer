package com.weather.services.core.interfaces;

import java.util.List;
import java.util.Map;

import com.weather.model.Location;

public interface LocationData {
	
	public List<Location> getLocationsDataByName(String siteName);
	
	public Location getLocationDataByGeoposition(double lat, double lon);
	
	public Location responseToLocation(Map<String, Object> element);
	
}
