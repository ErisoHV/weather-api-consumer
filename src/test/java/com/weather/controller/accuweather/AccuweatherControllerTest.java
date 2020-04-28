package com.weather.controller.accuweather;

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

import com.weather.exception.LocationNotFoundException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.CurrentWeatherStatusService;

@RunWith(MockitoJUnitRunner.class)
public class AccuweatherControllerTest {

	@Mock
	CurrentWeatherStatusService service;
	
	CurrentWeatherStatus current;
	
	AccuweatherController controller;
	@Before
	public void configure() {
		current = new CurrentWeatherStatus();
		current.setLocation(new Location());
		current.getLocation().setLongitude(-75.6373785784056);
		current.getLocation().setLatitude( 6.27780191817659);
		current.setTemperature(16);
		current.setWeatherServiceName("ACCUWEATHER");
		controller = new AccuweatherController(service);
	}
	
	
	@Test
	public void testGetCurrentWeatherController_notNull_test() {
		when(service
				.getCurrentAccuWeather(
				Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
			.thenReturn(current);
		
		ResponseEntity<?> currentWeather 
			= controller.getCurrentWeather("key", -75.6373785784056, 
					6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.OK));
		assertThat(currentWeather.getBody(), Matchers.notNullValue());
	}
	
	@Test
	public void testGetCurrentWeatherController_null_test() {
		when(service
				.getCurrentAccuWeather(
				Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
			.thenReturn(null);
		
		ResponseEntity<?> currentWeather 
			= controller.getCurrentWeather("key", -75.6373785784056, 
					6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.OK));
		assertThat(currentWeather.getBody(), Matchers.nullValue());	
	}
	
	@Test
	public void testGetCurrentWeatherController_error_test() {
		when(service
				.getCurrentAccuWeather(
				Mockito.anyDouble(), 
				Mockito.anyDouble(), 
				Mockito.any(), 
				Mockito.any()))
		.thenThrow(LocationNotFoundException.class);
		
		ResponseEntity<?> currentWeather 
			= controller.getCurrentWeather("key", -75.6373785784056, 
					6.27780191817659, null);
		assertThat(currentWeather, Matchers.notNullValue());
		assertThat(currentWeather.getStatusCode(), Matchers.is(HttpStatus.NOT_FOUND));
		assertThat(currentWeather.getBody(), Matchers.is("{\"error\" : \"Location not found\"}"));	
	}
	
}
