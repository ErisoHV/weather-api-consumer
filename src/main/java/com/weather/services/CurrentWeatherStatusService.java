package com.weather.services;

import org.springframework.stereotype.Service;

import com.weather.exception.LocationNotFoundException;
import com.weather.exception.WeatherServiceException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.apixu.ApixuService;
import com.weather.services.darksky.DarkSkyService;
import com.weather.services.language.Language;
import com.weather.services.openweather.OpenWeatherService;

@Service
public class CurrentWeatherStatusService {
	
	private CurrentWeatherStatusService() {
		// Empty constructor
	}
	
	public static CurrentWeatherStatus getCurrent(String serviceName, double latitude, double longitude, 
			Language lang, String key){
		
		if (serviceName == null)
			throw new WeatherServiceException("The service name cannot be null!");
		
		if (key == null || key.isEmpty())
			throw new WeatherServiceException("The key cannot be null or empty!");

		switch (serviceName.toUpperCase()) {
			case AccuWeatherService.SERVICE_NAME:
				return getCurrentAccuWeather( latitude,  longitude, lang, key);
			
			case ApixuService.SERVICE_NAME:
				return getCurrentDarkSkyService(latitude, longitude, lang, key);
			
			case DarkSkyService.SERVICE_NAME:
				
			break;
			case OpenWeatherService.SERVICE_NAME:
				
			break;
				
			default:
				break;
		}
		return null;
	}
	
	public static CurrentWeatherStatus getCurrentAccuWeather(double latitude, double longitude, 
			Language lang, String key){
		AccuWeatherService service = new AccuWeatherService().setKey(key);
		if (lang != null){
			service = service.setLanguage(lang).build();
		}
		Location loc = service.getLocationDataByGeoposition(latitude, longitude);
		if (loc != null){
			CurrentWeatherStatus current = service.getWeather(loc);
			if (current != null){
				return current;
			}
		}else{
			throw new LocationNotFoundException();
		}
		return null;
	}
	
	public static CurrentWeatherStatus getCurrentDarkSkyService(double latitude, double longitude, Language lang, String key){
		DarkSkyService service = new DarkSkyService().setKey(key);
		
		if (lang != null)
			service = service.setLanguage(lang).build();
		
		Location loc = new Location();
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		return service.getWeather(loc);
	}
	
	
	public static CurrentWeatherStatus getApixuService(double latitude, double longitude, Language lang, String key){
		ApixuService service = new ApixuService().setKey(key);
		if (lang != null)
			service = service.setLanguage(lang).build();
		return service.getWeatherByGeoposition(latitude, longitude);
	}
}
