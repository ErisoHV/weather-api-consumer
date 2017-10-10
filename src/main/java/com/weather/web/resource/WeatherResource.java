package com.weather.web.resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherResource {

	@GetMapping(value="/current", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getCurrentWeather(@RequestParam("lon") String longitude, @RequestParam("lat") String latitude){
		return longitude + ", " + latitude;
	}
	
	@GetMapping(value="/current/{api_name}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getCurrentWeather(@PathVariable("api_name") String apiName, @RequestParam("lon") String longitude, 
			@RequestParam("lat") String latitude){
		return longitude + ", " + latitude + " -> " + apiName;
	}
	
}
