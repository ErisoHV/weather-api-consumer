package com.weather.controller.apixu;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.model.CurrentWeatherStatus;
import com.weather.services.CurrentWeatherStatusService;
import com.weather.services.apixu.ApixuService;
import com.weather.services.language.Language;

@RestController
@RequestMapping("api/weather/apixu")
public class ApixuController {

	@GetMapping(value="/current", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getCurrentWeather(@RequestParam("key") String key, @RequestParam("lon") Double longitude, 
			@RequestParam("lat") Double latitude, @RequestParam(value="lang", required=false) Language language,
			@RequestParam(value="windvel", required = false) ApixuService.Velocity vel, 
			@RequestParam(value="temp", required=false) ApixuService.Temperature temp, 
			@RequestParam(value="vol", required=false) ApixuService.Volume vol){
			CurrentWeatherStatus current = 
					CurrentWeatherStatusService.getApixuService(latitude, longitude, language, key, vel, temp, vol);
			return new ResponseEntity<>(current, HttpStatus.OK);
	}
	
}
