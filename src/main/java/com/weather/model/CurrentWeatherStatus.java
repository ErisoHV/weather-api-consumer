package com.weather.model;

import java.sql.Timestamp;


public class CurrentWeatherStatus {
	private Location location;
	private Timestamp epochTime;
	private String weatherDescription;
	private int weatherIcon;
	private double temperature;
	private double realFeelTemperature;
	private String windDirection;
	private double windSpeed;
	private double precipitation;
	
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Timestamp getEpochTime() {
		return epochTime;
	}

	public void setEpochTime(Timestamp epochTime) {
		this.epochTime = epochTime;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public int getWeatherIcon() {
		return weatherIcon;
	}

	public void setWeatherIcon(int weatherIcon) {
		this.weatherIcon = weatherIcon;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getRealFeelTemperature() {
		return realFeelTemperature;
	}

	public void setRealFeelTemperature(double realFeelTemperature) {
		this.realFeelTemperature = realFeelTemperature;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}
}
