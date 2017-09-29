package com.weather.services.accuweather;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.WeatherService;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class AccuWeatherService extends WeatherService {
	private static final String GEOPOSITION_URL = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search";
	private static final String SEARCHTEXT_URL = "http://dataservice.accuweather.com/locations/v1/search";
	private static final String WEATHER_URL = "http://dataservice.accuweather.com/currentconditions/v1/";
	private static final String APIPARAM_NAME = "apikey";
	
	private AccuWeatherService(AccuWeatherService accu){
		super(accu.getApiKey(), APIPARAM_NAME);
		setByApiQueryParam(true);
	}
	
	public AccuWeatherService() {
		setApiLanguage("en"); // default language
	}

	public AccuWeatherService setKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}
	
	public AccuWeatherService setLanguage(String lang){
		setApiLanguage(lang);
		return this;
	}
	
	public AccuWeatherService build(){
		if (!isValidKey())
			throw new IllegalArgumentException("apiKey cannot be null or empty. Use setKey");
		return new AccuWeatherService(this);
	}
	
	@Override
	public List<Location> getLocationsDataByName(String siteName) {
		validateApiQueryParam();
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", siteName);
		params.put("details", "false");
		params.put("language", getApiLanguage());
		
		ResponseEntity<List> response = getAPIWeatherResponseEntityList(SEARCHTEXT_URL, params);
		List<Location> locations = new ArrayList<>();
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			
			List<Map<String, Object>> body = response.getBody();
			if (body != null && !body.isEmpty()){
				Location loc;
				for (Map<String, Object> element : body){
					loc = responseToLocation(element);
					locations.add(loc);
				}
			}
		} else{
			System.err.println("[AccuWeatherService -> getLocationsDataByName] ERROR = " + response);
		}
		return locations;
	}

	@Override
	public Location getLocationDataByGeoposition(double lat, double lon) {
		validateApiQueryParam();
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", lat + "," + lon);
		params.put("details", "false");
		params.put("language", getApiLanguage());
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(GEOPOSITION_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			return responseToLocation(response.getBody());
		} else{
			System.err.println("[AccuWeatherService -> getLocationDataByGeoposition] ERROR = " + response);
		}
		return null;
	}

	@Override
	protected Location responseToLocation(Map<String, Object> element){
		Location loc = new Location();
		loc.setName((String) element.get("EnglishName"));
		loc.setLocalizedName((String) element.get("LocalizedName"));
		Map<String, Object> subElement = (Map<String, Object>) element.get("AdministrativeArea");
		if (subElement != null && !subElement.isEmpty()){
			loc.setAdministrativeAreaName((String) subElement.get("LocalizedName"));
		}
		subElement = (Map<String, Object>) element.get("Country");
		if (subElement != null && !subElement.isEmpty()){
			loc.setLocationCountryName((String) subElement.get("LocalizedName"));
		}
		subElement = (Map<String, Object>) element.get("Region");
		if (subElement != null && !subElement.isEmpty()){
			loc.setLocationRegionName((String) subElement.get("LocalizedName"));
		}
		loc.setServiceKey((String) element.get("Key"));
		loc.setLocationType((String) element.get("Type"));
		
		subElement = (Map<String, Object>) element.get("GeoPosition");
		if (subElement != null && !subElement.isEmpty()){
			loc.setLongitude((double) subElement.get("Longitude"));
			loc.setLatitude((double) subElement.get("Latitude"));
		}
		return loc;
	}

	@Override
	public CurrentWeatherStatus getWeather(Location site) {
		validateApiQueryParam();
		Map<String, String> params = new HashMap<String, String>();
		params.put("details", "true");
		params.put("language", "es");
		// AccuWeather checks the weather with an internal key
		if (site.getServiceKey() == null || site.getServiceKey().isEmpty()){
			throw new IllegalArgumentException("The Service Key cannot be null, AccuWeather checks the weather with an internal key");
		}
		ResponseEntity<List> response = getAPIWeatherResponseEntityList(WEATHER_URL + site.getServiceKey(), params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			List<Map<String, Object>> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body.get(0), site);
			}
			
		} else{
			System.err.println("[AccuWeatherService -> HistoricoClimaDTO] ERROR = " + response.getBody());
		}
		
		return null;
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		weather.setEpochTime(new Timestamp(Long.valueOf((Integer) element.get("EpochTime"))));
		weather.setWeatherDescription((String) element.get("WeatherText"));
		weather.setWeatherIcon(String.valueOf((Integer) element.get("WeatherIcon")));
		Map<String, Map<String, Object>> subElement = (Map<String, Map<String, Object>>) element.get("Temperature");
		if (subElement != null && subElement.get("Metric") != null){
			weather.setTemperature((double) subElement.get("Metric").get("Value"));
		}
		subElement = (Map<String, Map<String, Object>>) element.get("RealFeelTemperature");
		if (subElement != null && subElement.get("Metric") != null){
			weather.setRealFeelTemperature((double) subElement.get("Metric").get("Value"));
		}
		subElement = (Map<String, Map<String, Object>>) element.get("Wind");
		if (subElement != null){
			if (subElement.get("Direction") != null){
				weather.setWindDirection((String) subElement.get("Direction").get("Localized"));
			}
			if (subElement.get("Speed") != null && subElement.get("Speed").get("Metric") != null){
				Map<String, Object> subSubElement = (Map<String, Object>) subElement.get("Speed").get("Metric");
				weather.setWindSpeed((double) subSubElement.get("Value"));
			}
		}
		subElement = (Map<String, Map<String, Object>>) element.get("PrecipitationSummary");
		if (subElement != null){
			Map<String, Map<String, Object>> subSubElement = (Map<String, Map<String, Object>>) element.get("PrecipitationSummary");
			if (subSubElement != null && subSubElement.get("Precipitation") != null){
				 Map<String, Object> subSubSubElement = (Map<String, Object>) subSubElement.get("Precipitation").get("Metric");
				 weather.setPrecipitation((double) subSubSubElement.get("Value"));
			}
		}
		
		weather.setLocation(loc);
		
		return weather;
	}
}
