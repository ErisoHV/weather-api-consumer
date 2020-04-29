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
		CurrentWeatherStatus current = null;
		HttpStatus status = HttpStatus.OK;
		String message = null;
		try {
			current = service.getCurrentAccuWeather(latitude, longitude, language, key);
		} catch (LocationNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			message = "Location not found: [" + longitude + "," + latitude + "]";
		} catch (WeatherServiceKeyException | WeatherServiceException e) {
			status = HttpStatus.BAD_REQUEST;
			message = e.getMessage();
		}
		response.setWeather(current);
		response.setStatus(status);
		response.setMessage(message);
		return new ResponseEntity<WeatherResponse>(response, status);
	}

	public AccuweatherController(CurrentWeatherStatusService service) {
		this.service = service;
	}
}
