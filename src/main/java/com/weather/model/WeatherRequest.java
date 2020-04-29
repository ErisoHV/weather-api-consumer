package com.weather.model;

import com.weather.services.apixu.ApixuService.Temperature;
import com.weather.services.apixu.ApixuService.Velocity;
import com.weather.services.apixu.ApixuService.Volume;
import com.weather.services.core.common.language.Language;

import lombok.Data;

@Data
public class WeatherRequest {
	private Location location;
	private String key;
	private Language language;
	
	private Temperature tempUnit;
	private Volume precipUnit;
	private Velocity windUnit;
	
}
