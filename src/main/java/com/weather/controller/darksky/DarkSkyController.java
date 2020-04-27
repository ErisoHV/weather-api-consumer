package com.weather.controller.darksky;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.model.CurrentWeatherStatus;
import com.weather.services.core.CurrentWeatherStatusService;
import com.weather.services.core.common.language.Language;

@RestController
@RequestMapping("api/weather/darksky")
public class DarkSkyController {

	@GetMapping(value="/current", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentWeather(@RequestParam("key") String key, @RequestParam("lon") Double longitude, 
			@RequestParam("lat") Double latitude, 
			@RequestParam(value="lang", required=false) Language language){
			CurrentWeatherStatus current = 
					CurrentWeatherStatusService.getCurrentDarkSkyService(latitude, longitude, language, key);
			return new ResponseEntity<>(current, HttpStatus.OK);
	}
	
}
