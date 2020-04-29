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
import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.WeatherResponse;
import com.weather.services.CurrentWeatherStatusService;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.core.common.language.Language;

@RestController
@RequestMapping("api/weather/accuweather")
public class AccuweatherController {

	@Autowired
	CurrentWeatherStatusService service;

	/**
	 * @param key
	 * @param longitude
	 * @param latitude
	 * @param language
	 * @return
	 */
	@GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WeatherResponse> getCurrentWeather(@RequestParam("key") String key,
			@RequestParam("lon") Double longitude, @RequestParam("lat") Double latitude,
			@RequestParam(value = "lang", required = false) Language language) {
		
		WeatherResponse response = new WeatherResponse();
		response.setService(AccuWeatherService.SERVICE_NAME);
		try {
			CurrentWeatherStatus current = service.getCurrentAccuWeather(latitude, 
					longitude, language, key);
			response.setWeather(current);
			response.setStatus(HttpStatus.OK);
			return new ResponseEntity<WeatherResponse>(response, HttpStatus.OK);
		} catch (LocationNotFoundException e) {
			response.setWeather(null);
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessage("Location not found: [" + longitude + "," + latitude + "]");
			return new ResponseEntity<WeatherResponse>(response, HttpStatus.NOT_FOUND);
		} catch (WeatherServiceKeyException | WeatherServiceException e) {
			response.setWeather(null);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<WeatherResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

	public AccuweatherController(CurrentWeatherStatusService service) {
		this.service = service;
	}
}
