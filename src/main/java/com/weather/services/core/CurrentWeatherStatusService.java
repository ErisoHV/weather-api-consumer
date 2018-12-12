package com.weather.services.core;

import org.springframework.stereotype.Service;

import com.weather.exception.LocationNotFoundException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.apixu.ApixuService;
import com.weather.services.darksky.DarkSkyService;
import com.weather.services.language.Language;

@Service
public class CurrentWeatherStatusService {
	
	private CurrentWeatherStatusService() {
		// Empty constructor
	}
	
	public static CurrentWeatherStatus getCurrentAccuWeather(double latitude, double longitude, 
			Language lang, String key){
		AccuWeatherService service = new AccuWeatherService().setKey(key);
		if (lang != null){
			service = service.setLanguage(lang).build();
		}
		Location loc = service.getLocationDataByGeoposition(latitude, longitude);
		if (loc != null){
			return service.getWeather(loc);
		}else{
			throw new LocationNotFoundException();
		}
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
	
	
	public static CurrentWeatherStatus getApixuService(double latitude, double longitude, Language lang, 
			String key, ApixuService.Velocity vel, ApixuService.Temperature temp, ApixuService.Volume vol){
		ApixuService service = new ApixuService().setKey(key);
		if (lang != null)
			service = service.setLanguage(lang).build();
		
		if (vel != null)
			service = service.setWindUnit(vel);
		
		if (temp != null)
			service = service.setTempUnit(temp);
		
		if (vol != null)
			service = service.setPrecipUnit(vol);
		
		return service.getWeatherByGeoposition(latitude, longitude);
	}
}
