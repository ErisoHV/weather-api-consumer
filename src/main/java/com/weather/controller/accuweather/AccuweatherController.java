package com.weather.controller.accuweather;

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
import com.weather.services.language.Language;

@RestController
@RequestMapping("api/weather/accuweather")
public class AccuweatherController {

	@GetMapping(value="/current", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getCurrentWeather(@RequestParam("key") String key, @RequestParam("lon") Double longitude, 
			@RequestParam("lat") Double latitude, @RequestParam(value="lang", required=false) Language language){
		try{
			CurrentWeatherStatus current = 
					CurrentWeatherStatusService.getCurrentAccuWeather(latitude, longitude, language, key);
			return new ResponseEntity<CurrentWeatherStatus>(current, HttpStatus.OK);
		} catch(LocationNotFoundException e){
			return new ResponseEntity<String>("Location not found", HttpStatus.NOT_FOUND);
		}
	}
}
