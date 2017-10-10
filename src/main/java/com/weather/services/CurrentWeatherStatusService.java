package com.weather.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.weather.exception.LocationNotFoundException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.language.Language;

@Service
public class CurrentWeatherStatusService {
	
	
	public CurrentWeatherStatus getCurrentAccuWeather(double latitude, double longitude, 
			Language lang, String key) throws LocationNotFoundException{
		AccuWeatherService service = new AccuWeatherService().setKey(key).setLanguage(lang).build();
		Location loc = service.getLocationDataByGeoposition(latitude, longitude);
		if (loc != null){
			CurrentWeatherStatus current = service.getWeather(loc);
			if (current != null){
				return current;
			} else{
				
			}
			
		}else{
			throw new LocationNotFoundException();
		}
		return null;
	}
	
	public List<CurrentWeatherStatus> getWeathers(double latitude, double longitude){
		List<CurrentWeatherStatus> weatherList = new ArrayList<>();
		
		
		
		return weatherList;
	}
	
}
