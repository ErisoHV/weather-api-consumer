package com.weather.model;

import java.sql.Timestamp;


public class CurrentWeatherStatus {
	private Location location;
	private Timestamp epochTime;
	private String weatherDescription;
	private String weatherIcon;
	private double temperature;
	private double realFeelTemperature;
	private String windDirection;
	private double windSpeed;
	private double precipitation;
	private String weatherServiceName;
	
	public CurrentWeatherStatus() {
		// Empty
	}
	
	public CurrentWeatherStatus(Location location, Timestamp epochTime, String weatherDescription, String weatherIcon,
			double temperature, double realFeelTemperature, String windDirection, double windSpeed,
			double precipitation, String weatherServiceName) {
		super();
		this.location = location;
		this.epochTime = epochTime;
		this.weatherDescription = weatherDescription;
		this.weatherIcon = weatherIcon;
		this.temperature = temperature;
		this.realFeelTemperature = realFeelTemperature;
		this.windDirection = windDirection;
		this.windSpeed = windSpeed;
		this.precipitation = precipitation;
		this.weatherServiceName = weatherServiceName;
	}

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

	public String getWeatherIcon() {
		return weatherIcon;
	}

	public void setWeatherIcon(String weatherIcon) {
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
	
	public String getWeatherServiceName() {
		return weatherServiceName;
	}

	public void setWeatherServiceName(String weatherServiceName) {
		this.weatherServiceName = weatherServiceName;
	}

	@Override
	public String toString() {
		return "CurrentWeatherStatus [location=" + location + ", epochTime="
				+ epochTime + ", weatherDescription=" + weatherDescription
				+ ", weatherIcon=" + weatherIcon + ", temperature="
				+ temperature + ", realFeelTemperature=" + realFeelTemperature
				+ ", windDirection=" + windDirection + ", windSpeed="
				+ windSpeed + ", precipitation=" + precipitation
				+ ", weatherServiceName=" + weatherServiceName + "]";
	}
	
}
