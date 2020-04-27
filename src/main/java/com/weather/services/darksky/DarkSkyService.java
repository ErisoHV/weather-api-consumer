package com.weather.services.darksky;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.core.WeatherService;
import com.weather.services.core.common.language.Language;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DarkSkyService extends WeatherService{
	public static final String WEATHER_URL = "https://api.darksky.net/forecast/";
	public static final String SERVICE_NAME = "DARKSKY";

	private DarkSkyService(DarkSkyService service){
		super(service.getApiKey(), "");
		setByApiQueryParam(false);
		setApiLanguage(service.getApiLanguage());
	}
	
	public DarkSkyService() {
		setApiLanguage(Language.en);
	}

	public DarkSkyService setKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}
	
	public DarkSkyService setLanguage(Language lang){
		if (lang != null)
			setApiLanguage(lang);
		
		return this;
	}
	
	public DarkSkyService build(){
		if (!isValidKey())
			throw new WeatherServiceKeyException();
		
		return new DarkSkyService(this);
	}

	@Override
	public CurrentWeatherStatus getWeather(Location site) {
		List<String> params = new ArrayList<>();
		params.add(site.getLatitude() + "," + site.getLongitude());
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("lang", getApiLanguage().toString());
		
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(WEATHER_URL, queryParams, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, site);
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
}
