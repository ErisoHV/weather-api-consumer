package com.weather.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.weather.model.CurrentWeatherStatus;
import com.weather.model.WeatherLocation;
import com.weather.properties.AccuWeatherConfigProperties;

@Service
public class AccuWeatherService extends WeatherService {

	@Autowired
	private AccuWeatherConfigProperties conf;
	
	public void test(){
		System.out.println(conf.getAccuweatherKeyParamName());
	}
	
	
	@Override
	public WeatherLocation getLocationDataByName(String siteName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(conf.getAccuweatherKeyParamName(), conf.getAccuweatherKey());
		params.put("q", siteName);
		params.put("details", "false");
		ResponseEntity<List> response = getResponseEntity(conf.getGeopositionURL(), params);
		if (response.getStatusCode().equals(HttpStatus.OK)){
			List<Map<String, Object>> body = response.getBody();
			
			if (body != null && !body.isEmpty() && body.get(0) != null){
				System.out.println(body);
				System.out.println(body.get(0).get("Version"));
			}
			
		} else{
			System.err.println("[AccuWeatherService -> getSiteDataByName] ERROR = " + response.getBody());
		}
		return null;
	}

	@Override
	public WeatherLocation getSiteDataByGeoposition(double lat, double lon) {
		// Empty method
		return null;
	}

	@Override
	public CurrentWeatherStatus getWeather(WeatherLocation site) {
		return null;
	}

	

}
