package com.weather.controller.darksky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.WeatherResponse;
import com.weather.services.CurrentWeatherStatusService;
import com.weather.services.core.common.language.Language;
import com.weather.services.darksky.DarkSkyService;

@RestController
@RequestMapping("api/weather/darksky")
public class DarkSkyController {
	
	@Autowired
	CurrentWeatherStatusService service;

	@GetMapping(value="/current", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WeatherResponse> getCurrentWeather(@RequestParam("key") String key, @RequestParam("lon") Double longitude, 
			@RequestParam("lat") Double latitude, 
			@RequestParam(value="lang", required=false) Language language){
			
			WeatherResponse response = new WeatherResponse();
			response.setService(DarkSkyService.SERVICE_NAME);
			CurrentWeatherStatus current = null;
			HttpStatus status = HttpStatus.OK;
			String message = null;
			try {
				current = service.getCurrentDarkSkyService(latitude, longitude, language, key);
			} catch (WeatherServiceKeyException e) {
				status = HttpStatus.BAD_REQUEST;
				message = e.getMessage();
			}
			response.setWeather(current);
			response.setStatus(status);
			response.setMessage(message);
			return new ResponseEntity<>(response, status);
	}
	
}


