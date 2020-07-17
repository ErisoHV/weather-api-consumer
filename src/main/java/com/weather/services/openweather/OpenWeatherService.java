package com.weather.services.openweather;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.weather.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.model.WeatherRequest;
import com.weather.services.core.WeatherService;

/**
 * https://openweathermap.org
 * 
 * @author ErisoHV
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OpenWeatherService extends WeatherService{
	public static final String SEARCHTEXT_URL = "http://bulk.openweathermap.org/sample/city.list.json.gz";
	public static final String WEATHER_URL = "http://samples.openweathermap.org/data/2.5/weather";
	public static final String APIPARAM_NAME = "appid";
	
	public static final String SERVICE_NAME = "OPENWEATHER";
	
	private  static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherService.class);
	
	
	@Override
	public CurrentWeatherStatus getWeather(WeatherRequest request) {
		Map<String, String> params = new LinkedHashMap<>();
		if (RequestUtils.isOKWeatherKeyRequest(request)){
			params.put("id", request.getKey());
		} else if (RequestUtils.isOKLocationRequest(request)){
			params.put ("lat", String.valueOf(request.getLocation().getLatitude()));
			params.put ("lon", String.valueOf(request.getLocation().getLongitude()));
		} else if (RequestUtils.isOKNameLocationRequest(request)){
			params.put("q", request.getLocation().getName());
		} else{
			throw new IllegalArgumentException("Invalid Location");
		}
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(WEATHER_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, request.getLocation());
			}
		} else{
			LOGGER.error("[AccuWeatherService -> getLocationsDataByName] ERROR = " + response);
		}
		return null;
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(
			Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		if (element.get("weather") != null ){
			List<Map<String, Object>> weatherList = (List<Map<String, Object>>) element.get("weather");
			if (weatherList != null && !weatherList.isEmpty() && weatherList.get(0) != null){
				weather.setWeatherDescription((String) weatherList.get(0).get("description"));
				weather.setWeatherIcon((String) weatherList.get(0).get("icon"));
			}
			
			Map<String, Object> weatherMap = (Map<String, Object>) element.get("wind");
			if (weatherMap != null && !weatherMap.isEmpty()){
				weather.setWindSpeed((double) weatherMap.get("speed"));
			}
			
			weatherMap = (Map<String, Object>) element.get("main");
			if (weatherMap != null && !weatherMap.isEmpty()){
				weather.setTemperature((double) weatherMap.get("temp"));
			}
			weather.setEpochTime(new Timestamp(Long.valueOf((Integer) element.get("dt"))));
		}
		// Complete weather information
		loc.setServiceKey(String.valueOf(element.get("id")));
		weather.setLocation(loc);
		return weather;
	}
	
}
