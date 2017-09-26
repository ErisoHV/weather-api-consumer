package com.weather.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:accuweather.properties")
public class AccuWeatherConfigProperties {
	@Value("${accuweather.api.paramname}")
	private String accuweatherKeyParamName;
	
	@Value("${accuweather.api.key}")
	private String accuweatherKey;
	
	@Value("${accuweather.url.geoposition}")
	private String geopositionURL;
	
	@Value("${accuweather.url.weather}")
	private String weatherURL;
	
	public String getAccuweatherKeyParamName() {
		return accuweatherKeyParamName;
	}

	public void setAccuweatherKeyParamName(String accuweatherKeyParamName) {
		this.accuweatherKeyParamName = accuweatherKeyParamName;
	}
	
	public String getAccuweatherKey() {
		return accuweatherKey;
	}

	public void setAccuweatherKey(String gMapReportUrl) {
		this.accuweatherKey = gMapReportUrl;
	}
	
	public String getGeopositionURL() {
		return geopositionURL;
	}

	public void setGeopositionURL(String geopositionURL) {
		this.geopositionURL = geopositionURL;
	}

	public String getWeatherURL() {
		return weatherURL;
	}

	public void setWeatherURL(String weatherURL) {
		this.weatherURL = weatherURL;
	}
}
