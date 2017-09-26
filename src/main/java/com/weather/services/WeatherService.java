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
import com.weather.model.WeatherLocation;

public abstract class WeatherService {

	public static final String TEMP_SYMBOL = "&deg;";
	public static final String VEL_SYMBOL = "km/h";
	public static final String PREC_SYMBOL = "mm";

	protected ResponseEntity<List> getResponseEntity(String url, Map<String, String> params){
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		if (params != null){
			for (Entry<String, String> entry : params.entrySet()){
				builder = builder.queryParam(entry.getKey() , entry.getValue());
			}
		}
		
		UriComponents urlBuilded = builder.build().encode();
		System.err.println(urlBuilded.toUriString());
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

	public abstract WeatherLocation getLocationDataByName(String siteName);
	
	public abstract WeatherLocation getSiteDataByGeoposition(double lat, double lon);
	
	public abstract CurrentWeatherStatus getWeather(WeatherLocation site);
	
}
