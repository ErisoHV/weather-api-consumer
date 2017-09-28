package com.weather.model;

public class Location {
	private long locationID;
	private String name;
	private String localizedName;
	private String administrativeAreaName;
	private String locationCountryName;
	private String locationRegionName;
	private String serviceKey;
	private String abbreviation;
	private String locationType;
	private double longitude;
	private double latitude;

	public long getLocationID() {
		return locationID;
	}
	
	public void setLocationID(long locationID) {
		this.locationID = locationID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String locationName) {
		this.name = locationName;
	}
	
	public String getLocalizedName() {
		return localizedName;
	}

	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}

	public String getAdministrativeAreaName() {
		return administrativeAreaName;
	}
	
	public void setAdministrativeAreaName(String locationCity) {
		this.administrativeAreaName = locationCity;
	}
	
	public String getLocationCountryName() {
		return locationCountryName;
	}
	
	public void setLocationCountryName(String locationCountry) {
		this.locationCountryName = locationCountry;
	}
	
	public String getLocationRegionName() {
		return locationRegionName;
	}

	public void setLocationRegionName(String locationRegionName) {
		this.locationRegionName = locationRegionName;
	}

	public String getServiceKey() {
		return serviceKey;
	}
	
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Location [locationID=" + locationID + ", name=" + name
				+ ", localizedName=" + localizedName
				+ ", administrativeAreaName=" + administrativeAreaName
				+ ", locationCountryName=" + locationCountryName
				+ ", locationRegionName=" + locationRegionName
				+ ", serviceKey=" + serviceKey + ", abbreviation="
				+ abbreviation + ", locationType=" + locationType
				+ ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
}
