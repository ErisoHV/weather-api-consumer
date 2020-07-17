package com.weather.controller.darksky;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.model.WeatherResponse;
import com.weather.services.CurrentWeatherStatusService;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.darksky.DarkSkyService;

@RunWith(MockitoJUnitRunner.class)
public class DarkSkyControllerTest {

	@Mock
	private CurrentWeatherStatusService service;
	
	private CurrentWeatherStatus current;
	
	private DarkSkyController controller;
	
	private WeatherResponse response;
	
	@Before
	public void configure() {
		current = new CurrentWeatherStatus();
		current.setLocation(new Location());
		current.getLocation().setLongitude(-75.6373785784056);
		current.getLocation().setLatitude( 6.27780191817659);
		current.setTemperature(16);
		controller = new DarkSkyController(service);
		
		response = new WeatherResponse();
		response.setWeather(current);
		response.setStatus(HttpStatus.OK);
		response.setService(AccuWeatherService.SERVICE_NAME);
	}
	
	
	@Test
	public void getCurrentWeather_weatherIsnotNull_test() {
		when(service.getCurrentDarkSkyService(Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
			.thenReturn(current);
		
		ResponseEntity<WeatherResponse> currentWeather 
				= controller.getCurrentWeather("key", -75.6373785784056, 6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.OK));
		assertThat(currentWeather.getBody(), Matchers.notNullValue());
		assertThat(currentWeather.getBody().getWeather(), Matchers.notNullValue());
		assertThat(currentWeather.getBody().getService(), 
				Matchers.is(DarkSkyService.SERVICE_NAME));
	}
	
	@Test
	public void getCurrentWeatherController_wheatherIsNull_test() {
		when(service
				.getCurrentDarkSkyService(
				Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
			.thenReturn(null);
		
		ResponseEntity<WeatherResponse> currentWeather 
			= controller.getCurrentWeather("key", -75.6373785784056, 
					6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.OK));
		assertThat(currentWeather.getBody(), Matchers.notNullValue());
		assertThat(currentWeather.getBody().getWeather(), Matchers.nullValue());
		
	}
	
	@Test
	public void getCurrentWeatherController_weatherServiceKeyError_test() {
		when(service
				.getCurrentDarkSkyService(
				Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
		.thenThrow(new WeatherServiceKeyException());
		
		ResponseEntity<WeatherResponse> currentWeather 
			= controller.getCurrentWeather("key", -75.6373785784056, 
					6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.BAD_REQUEST));
		assertThat(currentWeather.getBody(), Matchers.notNullValue());
		assertThat(currentWeather.getBody().getWeather(), Matchers.nullValue());
		assertThat(currentWeather.getBody().getMessage(), 
				Matchers.is("The Service Key cannot be null, missing key"));
	}
	
	@Test
	public void getCurrentWeatherController_weatherServiceError_test() {
		when(service
				.getCurrentDarkSkyService(
				Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
		.thenThrow(new WeatherServiceException("Error"));
		
		ResponseEntity<WeatherResponse> currentWeather 
			= controller.getCurrentWeather("key", -75.6373785784056, 
					6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.BAD_REQUEST));
		assertThat(currentWeather.getBody(), Matchers.notNullValue());
		assertThat(currentWeather.getBody().getWeather(), Matchers.nullValue());
		assertThat(currentWeather.getBody().getMessage(), 
				Matchers.is("Error"));
	}
	
}
