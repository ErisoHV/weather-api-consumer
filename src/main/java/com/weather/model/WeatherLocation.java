package com.weather.model;

public class WeatherLocation {
	private long locationID;
	private String locationName;
	private String locationCity;
	private String locationCountry;
	private String serviceKey;
	private boolean isActive;
	private String abbreviation;

	public long getLocationID() {
		return locationID;
	}
	
	public void setLocationID(long locationID) {
		this.locationID = locationID;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public String getLocationCity() {
		return locationCity;
	}
	
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	
	public String getLocationCountry() {
		return locationCountry;
	}
	
	public void setLocationCountry(String locationCountry) {
		this.locationCountry = locationCountry;
	}
	
	public String getServiceKey() {
		return serviceKey;
	}
	
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
}
