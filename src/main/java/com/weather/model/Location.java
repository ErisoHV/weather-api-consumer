package com.weather.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
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
	private Double longitude;
	private Double latitude;

}
