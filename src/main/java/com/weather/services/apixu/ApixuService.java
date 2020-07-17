package com.weather.services.apixu;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.weather.exception.WeatherServiceException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.model.WeatherRequest;
import com.weather.services.core.WeatherService;
import com.weather.services.core.interfaces.LocationData;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public class ApixuService extends WeatherService implements LocationData{
	public static final String SEARCHTEXT_URL = "http://api.apixu.com/v1/search.json";
	public static final String WEATHER_URL = "http://api.apixu.com/v1/current.json";
	public static final String APIPARAM_NAME = "key";
	
	public static final String SERVICE_NAME = "APIXU";
	
	public enum Temperature {
		/**
		 * Temperature constant for Celcius = c
		 */
		c,
		/**
		 * Temperature constant for Fahrenheit = f
		 */
		f
	}
	
	public enum Velocity {
		/**
		 * Meters per hour constant = mph
		 */
		mph,
		
		/**
		 * Kilometers per hour constant = kph
		 */
		kph
	}

	public enum Volume {
		/**
		 * Milimetre constant = mm
		 */
		mm,
		
		/**
		 * Milimetre constant = mm
		 */
		in
	}

	@Override
	public List<Location> getLocationsDataByName(WeatherRequest request) {
		return getLocations(request, true);
	}

	@Override
	public Location getLocationDataByGeoposition(WeatherRequest request) {
		ArrayList<Location> list= (ArrayList<Location>) getLocations(request, false);
		if (!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	private List<Location> getLocations(WeatherRequest request, boolean byName){
		Map<String, String> params = new LinkedHashMap<>();
		if (byName) {
			params.put("q", request.getLocation().getName());
		} else {
			params.put("q", request.getLocation().getLatitude() 
					+ "," + request.getLocation().getLongitude());
		}
		
		if (request.getLanguage() != null)
			params.put("lang", request.getLanguage().toString());
		
		ResponseEntity<List> response 
			= getAPIWeatherResponseEntityList(SEARCHTEXT_URL, params);
		List<Location> locations = new ArrayList<>();
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			
			List<Map<String, Object>> body = response.getBody();
			if (body != null && !body.isEmpty()){
				Location loc;
				for (Map<String, Object> element : body){
					loc = responseToLocation(element);
					locations.add(loc);
				}
			}
		} else{
			throw new WeatherServiceException(response);
		}
		return locations;
	}
	
	public CurrentWeatherStatus getWeatherByName(WeatherRequest request){
		if (request.getLocation().getName() == null 
				|| request.getLocation().getName() .isEmpty())
			throw new IllegalArgumentException("The location name cannot be null or empty");
		
		Location loc = new Location();
		loc.setName(request.getLocation().getName());
		return getWeather(request);
	}
	
	public CurrentWeatherStatus getWeatherByGeoposition(WeatherRequest request){
		Map<String, String> params = new HashMap<>();
		params.put("q", request.getLocation().getLatitude() 
				+ ", " + request.getLocation().getLongitude());
		params.put("lang", request.getLanguage().toString());

		return callToApixuWeatherService(request, false);
	}

	/**
	 * Get the weather by location name
	 * @param site The location with a defined name
	 * @return  CurrentWeatherStatus
	 */
	@Override
	public CurrentWeatherStatus getWeather(WeatherRequest request) {
		Map<String, String> params = new HashMap<>();
		params.put("q", request.getLocation().getName());
		params.put("lang", request.getLanguage().toString());

		return callToApixuWeatherService(request, true);
	}
	
	private CurrentWeatherStatus callToApixuWeatherService(WeatherRequest request, 
			boolean byName){
		Map<String, String> params = new HashMap<>();
		if (byName) {
			params.put("q", request.getLocation().getName());
		} else {
			params.put("q", request.getLocation().getLatitude() 
					+ ", " + request.getLocation().getLongitude());
		}
			
		if (request.getLanguage() != null)
			params.put("lang", request.getLanguage().toString());
		
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(WEATHER_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, request.getLocation());
			}
		} else{
			throw new WeatherServiceException(WeatherServiceException.buildErrorResponse(response));
		}
		return null;
	}

	@Override
	public Location responseToLocation(Map<String, Object> element) {
		Location loc = new Location();
		loc.setServiceKey(String.valueOf(element.get("id")));
		loc.setName((String) element.get("name"));
		loc.setLocationRegionName((String) element.get("region"));
		loc.setLocationCountryName((String) element.get("country"));
		
		loc.setLatitude((double) element.get("lat"));
		loc.setLongitude((double) element.get("lon"));

		return loc;
	}

	protected CurrentWeatherStatus responseToWeather(
			Map<String, Object> element, WeatherRequest request) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		Map<String, Object> subElement = (Map<String, Object>) element.get("current");
		if (subElement != null){
			weather.setEpochTime(new Timestamp(Long.valueOf((Integer) subElement.get("last_updated_epoch"))));
			weather.setTemperature((double) subElement.get("temp_" + request.getTempUnit()));
			weather.setWindSpeed((double) subElement.get("wind_" + request.getWindUnit()));
			weather.setWindDirection((String) subElement.get("wind_dir"));
			weather.setPrecipitation((double) subElement.get("precip_" + request.getPrecipUnit()));
			weather.setRealFeelTemperature((double) subElement.get("feelslike_" 
					+ request.getTempUnit()));
			
			Map<String, Object> subSubElement = 
					(Map<String, Object>) subElement.get("condition");
			if (subSubElement != null){
				weather.setWeatherDescription((String) subSubElement.get("text"));
				weather.setWeatherIcon((String) subSubElement.get("icon"));
			}
		}
		
		subElement = (Map<String, Object>) element.get("location");
		Location loc = null;
		if (subElement != null){
			loc = responseToLocation(subElement);
		}
		weather.setWeatherServiceName(SERVICE_NAME);
		weather.setLocation(loc);
		return weather;
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc) {
		return null;
	}
	
}
