package com.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.exception.LocationNotFoundException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.model.WeatherRequest;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.apixu.ApixuService;
import com.weather.services.core.common.language.Language;
import com.weather.services.darksky.DarkSkyService;

@Service
public class CurrentWeatherStatusService {

	@Autowired
	private DarkSkyService darkSkyService;
	
	@Autowired
	private AccuWeatherService accuweatherService;
	
	@Autowired
	private ApixuService apixuService;

	
	public CurrentWeatherStatus getCurrentAccuWeather(double latitude, double longitude, 
			Language lang, String key){
		WeatherRequest request = new WeatherRequest();
		request.setKey(key);
		request.setLocation(new Location());
		request.getLocation().setLongitude(longitude);
		request.getLocation().setLatitude(latitude);
		request.setLanguage(lang);
		
		request.setLocation(accuweatherService.getLocationDataByGeoposition(request));
		
		if (request.getLocation() != null){
			return accuweatherService.getWeather(request);
		} else {
			throw new LocationNotFoundException();
		}
	}
	
	/**
	 * Utiliza el servicio de DarkSky para consultar el clima
	 * @param latitude
	 * @param longitude
	 * @param lang
	 * @param key
	 * @return
	 */
	public CurrentWeatherStatus getCurrentDarkSkyService(double latitude, double longitude, 
			Language lang, String key){
		WeatherRequest request = new WeatherRequest();
		request.setKey(key);
		request.setLocation(new Location());
		request.getLocation().setLongitude(longitude);
		request.getLocation().setLatitude(latitude);
		request.setLanguage(lang);
		return darkSkyService.getWeather(request);
	}
	
	
	public CurrentWeatherStatus getApixuService(double latitude, double longitude, 
			Language lang, String key, ApixuService.Velocity vel, 
			ApixuService.Temperature temp, ApixuService.Volume vol){
		
		WeatherRequest request = new WeatherRequest();
		request.setKey(key);
		request.setLocation(new Location());
		request.getLocation().setLongitude(longitude);
		request.getLocation().setLatitude(latitude);
		request.setWindUnit(vel);
		request.setTempUnit(temp);
		request.setPrecipUnit(vol);
		request.setLanguage(lang);
		return apixuService.getWeatherByGeoposition(request);
	}
}
