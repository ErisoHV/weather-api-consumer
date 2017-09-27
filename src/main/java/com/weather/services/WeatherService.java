package com.weather.services;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;

public abstract class WeatherService {

	public static final String TEMP_SYMBOL = "&deg;";
	public static final String VEL_SYMBOL = "km/h";
	public static final String PREC_SYMBOL = "mm";

	/**
	 * Return a generic ResponseEntity List from any service
	 * @param url Service API URL
	 * @param params Query parameters
	 * @return ResponseEntity List
	 */
	protected ResponseEntity<List> getResponseEntityList(String url, Map<String, String> params){
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		if (params != null){
			for (Entry<String, String> entry : params.entrySet()){
				builder = builder.queryParam(entry.getKey() , entry.getValue());
			}
		}
		
		UriComponents urlBuilded = builder.build().encode();
		RestTemplate template  = new RestTemplate();
		try {
			ResponseEntity<List> map = template.getForEntity(urlBuilded.toUriString(), List.class);
				if (map.getStatusCode().equals(HttpStatus.OK)){
					return map;
				}
				else{
					System.err.println("Location not found");
				}
		} catch (RestClientException e) {
			System.err.println("Rest Client exception, check API key and query params. Response = " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Return a generic ResponseEntity Map from any service
	 * @param url Service API URL
	 * @param params Query parameters
	 * @return ResponseEntity Map
	 */
	protected ResponseEntity<Map> getResponseEntityMap(String url, Map<String, String> params){
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		if (params != null){
			for (Entry<String, String> entry : params.entrySet()){
				builder = builder.queryParam(entry.getKey() , entry.getValue());
			}
		}
		
		UriComponents urlBuilded = builder.build().encode();
		RestTemplate template  = new RestTemplate();
		try {
			ResponseEntity<Map> map = template.getForEntity(urlBuilded.toUriString(), Map.class);
				if (map.getStatusCode().equals(HttpStatus.OK)){
					return map;
				}
				else{
					System.err.println("Location not found");
				}
		} catch (RestClientException e) {
			System.err.println("Rest Client exception, check API key and query params. Response = " + e.getMessage());
		}
		return null;
	}

	public abstract List<Location> getLocationsDataByName(String siteName);
	
	public abstract Location getLocationDataByGeoposition(double lat, double lon);
	
	public abstract CurrentWeatherStatus getWeather(Location site);
	
	protected abstract Location responseToLocation(Map<String, Object> element);
	
	protected abstract CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc);
	
}
