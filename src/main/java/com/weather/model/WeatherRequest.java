package com.weather.model;

import com.weather.services.apixu.ApixuService.Temperature;
import com.weather.services.apixu.ApixuService.Velocity;
import com.weather.services.apixu.ApixuService.Volume;
import com.weather.services.core.common.language.Language;

public class WeatherRequest {
	private Location location;
	private String key;
	private Language language;
	
	private Temperature tempUnit;
	private Volume precipUnit;
	private Velocity windUnit;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Temperature getTempUnit() {
		return tempUnit;
	}
	public void setTempUnit(Temperature tempUnit) {
		this.tempUnit = tempUnit;
	}
	public Volume getPrecipUnit() {
		return precipUnit;
	}
	public void setPrecipUnit(Volume precipUnit) {
		this.precipUnit = precipUnit;
	}
	public Velocity getWindUnit() {
		return windUnit;
	}
	public void setWindUnit(Velocity windUnit) {
		this.windUnit = windUnit;
	}
}
