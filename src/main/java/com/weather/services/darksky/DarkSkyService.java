package com.weather.services.darksky;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.WeatherService;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DarkSkyService extends WeatherService{
	private static final String WEATHER_URL = "https://api.darksky.net/forecast/";

	private DarkSkyService(DarkSkyService accu){
		super(accu.getApiKey(), "");
		setByApiQueryParam(false);
	}
	
	public DarkSkyService() {
		// Empty Constructor
	}

	public DarkSkyService setKey(String apiKey) {
		if (isValidKey())
			throw new IllegalArgumentException("apiKey cannot be null or empty");
		
		this.setApiKey(apiKey);
		return this;
	}
	
	public DarkSkyService build(){
		if (!isValidKey())
			throw new IllegalArgumentException("apiKey cannot be null or empty");
		
		return new DarkSkyService(this);
	}
	
	
	@Override
	public List<Location> getLocationsDataByName(String siteName) {
		// Empty
		return null;
	}

	@Override
	public Location getLocationDataByGeoposition(double lat, double lon) {
		// Empty
		return null;
	}

	@Override
	public CurrentWeatherStatus getWeather(Location site) {
		List<String> params = new ArrayList<>();
		
		params.add(site.getLatitude() + "," + site.getLongitude());
		
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(WEATHER_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, site);
			}
			
		} else{
			System.err.println("[AccuWeatherService -> HistoricoClimaDTO] ERROR = " + response);
		}
		return null;
	}

	@Override
	protected Location responseToLocation(Map<String, Object> element) {
		
		return null;
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		Map<String,Object> map = (Map<String, Object>) element.get("currently");
		if (map != null){
			weather.setEpochTime(new Timestamp(Long.valueOf((Integer)map.get("time"))));
			weather.setWeatherDescription((String) map.get("summary"));
			weather.setWeatherIcon((String) map.get("icon"));
			weather.setPrecipitation((double) map.get("precipIntensity"));
			weather.setTemperature((double) map.get("temperature"));
			weather.setWindSpeed((double) map.get("windSpeed"));
		}
		
		weather.setLocation(loc);
		
		return weather;
	}

}
