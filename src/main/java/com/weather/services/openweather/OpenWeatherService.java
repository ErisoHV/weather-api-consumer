package com.weather.services.openweather;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.core.WeatherService;
import com.weather.services.language.Language;

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
	
	private static final Logger LOGGER = Logger.getLogger(OpenWeatherService.class);
	
	private OpenWeatherService(OpenWeatherService open){
		super(open.getApiKey(), APIPARAM_NAME);
		setByApiQueryParam(true);
	}
	
	public OpenWeatherService() {
		// Empty Constructor
	}
	
	public OpenWeatherService setLanguage(Language language){
		setApiLanguage(language);
		return this;
	}


	public OpenWeatherService setKey(String apiKey) {
		if (isValidKey())
			throw new IllegalArgumentException("apiKey cannot be null or empty");
		
		this.setApiKey(apiKey);
		return this;
	}
	
	public OpenWeatherService build(){
		if (!isValidKey())
			throw new IllegalArgumentException("apiKey cannot be null or empty");
		
		return new OpenWeatherService(this);
	}
	
	@Override
	public CurrentWeatherStatus getWeather(Location site) {
		validateApiQueryParam();
		Map<String, String> params = new LinkedHashMap<>();
		if (site.getServiceKey() != null && site.getServiceKey().isEmpty()){
			params.put("id", site.getServiceKey());
		} else if (site.getLatitude() != null && site.getLongitude() != null){
			params.put ("lat", String.valueOf(site.getLatitude()));
			params.put ("lon", String.valueOf(site.getLongitude()));
		} else if (site.getName() != null && !site.getName().isEmpty()){
			params.put("q", site.getName());
		} else{
			throw new IllegalArgumentException("Invalid Location");
		}
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(WEATHER_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, site);
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
