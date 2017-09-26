package com.weather.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weather.model.CurrentWeatherStatus;

@Service
public class WeatherProcessService {
	private Map<Long, CurrentWeatherStatus> weatherList = new HashMap<Long, CurrentWeatherStatus>();

	public Map<Long, CurrentWeatherStatus> getWeatherList() {
		return weatherList;
	}

	public void setWeatherList(Map<Long, CurrentWeatherStatus> weatherList) {
		this.weatherList = weatherList;
	}
}
