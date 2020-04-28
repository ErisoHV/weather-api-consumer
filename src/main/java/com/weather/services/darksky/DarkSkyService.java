package com.weather.services.darksky;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;

import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.model.WeatherRequest;
import com.weather.services.core.WeatherService;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class DarkSkyService extends WeatherService{
	public static final String WEATHER_URL = "https://api.darksky.net/forecast/";
	public static final String SERVICE_NAME = "DARKSKY";

	@Override
	public CurrentWeatherStatus getWeather(WeatherRequest request) {
		if (request.getKey() == null || request.getKey().isEmpty())
			throw new WeatherServiceKeyException();
		
		List<String> params = new ArrayList<>();
		params.add(request.getLocation().getLatitude() 
				+ "," + request.getLocation().getLongitude());
		
		Map<String, String> queryParams = new HashMap<>();
		if (request.getLanguage() != null && !request.getLanguage().toString().isEmpty())
			queryParams.put("lang", request.getLanguage().toString());
		
		setApiKey(request.getKey());
		
		ResponseEntity<Map> response 
			= getAPIWeatherResponseEntityMap(WEATHER_URL, queryParams, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, request.getLocation());
			}
			
		} else{
			throw new WeatherServiceException(response);
		}
		return null;
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		Map<String,Object> map = (Map<String, Object>) element.get("currently");
		if (map != null){
			weather.setEpochTime(new Timestamp(Long.valueOf((Integer)map.get("time"))));
			weather.setWeatherDescription((String) map.get("summary"));
			weather.setWeatherIcon((String) map.get("icon"));
			weather.setPrecipitation((int) map.get("precipIntensity"));
			weather.setTemperature((double) map.get("temperature"));
			weather.setWindSpeed((double) map.get("windSpeed"));
		}
		weather.setWeatherServiceName(SERVICE_NAME);
		weather.setLocation(loc);
		
		return weather;
	}
	
	private ResponseEntity<Map> getAPIWeatherResponseEntityMap(String url, 
			Map<String, String> urlParams, List<String> pathParams){
		UriComponents urlBuilded = buildURLParameters(url, urlParams, pathParams);
		return getAPIWeatherResponseEntityMap(urlBuilded);
	}
}
