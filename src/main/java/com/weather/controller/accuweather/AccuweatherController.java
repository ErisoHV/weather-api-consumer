package com.weather.controller.accuweather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.exception.LocationNotFoundException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.services.CurrentWeatherStatusService;
import com.weather.services.core.common.language.Language;

@RestController
@RequestMapping("api/weather/accuweather")
public class AccuweatherController {
	
	@Autowired
	CurrentWeatherStatusService service;
	

	@GetMapping(value="/current", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentWeather(@RequestParam("key") String key, @RequestParam("lon") Double longitude, 
			@RequestParam("lat") Double latitude, @RequestParam(value="lang", required=false) Language language){
		try{
			CurrentWeatherStatus current = 
					service.getCurrentAccuWeather(latitude, longitude, language, key);
			return new ResponseEntity<CurrentWeatherStatus>(current, HttpStatus.OK);
		} catch(LocationNotFoundException e){
			return new ResponseEntity<>("{\"error\" : \"Location not found\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	public AccuweatherController(CurrentWeatherStatusService service) {
		this.service = service;
	}
}
