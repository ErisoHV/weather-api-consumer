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

import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.WeatherService;
import com.weather.services.language.Language;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class AccuWeatherService extends WeatherService {
	private static final String GEOPOSITION_URL = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search";
	private static final String SEARCHTEXT_URL = "http://dataservice.accuweather.com/locations/v1/search";
	private static final String WEATHER_URL = "http://dataservice.accuweather.com/currentconditions/v1/";
	private static final String APIPARAM_NAME = "apikey";
	
	public static final String SERVICE_NAME = "ACCUWEATHER";
	
	private AccuWeatherService(AccuWeatherService accu){
		super(accu.getApiKey(), APIPARAM_NAME);
		setByApiQueryParam(true);
		setApiLanguage(accu.getApiLanguage());
	}
	
	public AccuWeatherService() {
		setApiLanguage(Language.en); // default language
	}

	public AccuWeatherService setKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}
	
	public AccuWeatherService setLanguage(Language lang){
		setApiLanguage(lang);
		return this;
	}
	
	public AccuWeatherService build(){
		if (!isValidKey())
			throw new WeatherServiceKeyException();
		return new AccuWeatherService(this);
	}
	
	@Override
	public List<Location> getLocationsDataByName(String siteName) {
		validateApiQueryParam();
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", siteName);
		params.put("details", "false");
		params.put("language", getApiLanguage().toString());
		
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
			if (response != null && response.getBody() != null)
				throw new WeatherServiceException(response.getBody().toString());
			else
				throw new WeatherServiceException("Response is null");
		}
		return locations;
	}

	@Override
	public Location getLocationDataByGeoposition(double lat, double lon) {
		validateApiQueryParam();
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", lat + "," + lon);
		params.put("details", "false");
		params.put("language", getApiLanguage().toString());
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(GEOPOSITION_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			return responseToLocation(response.getBody());
		} else{
			if (response != null && response.getBody() != null)
				throw new WeatherServiceException(response.getBody().toString());
			else
				throw new WeatherServiceException("Response is null");
		}
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
		params.put("language", getApiLanguage().toString());
		ResponseEntity<List> response = getAPIWeatherResponseEntityList(WEATHER_URL + site.getServiceKey(), params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			List<Map<String, Object>> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body.get(0), site);
			}
			
		} else{
			if (response != null && response.getBody() != null)
				throw new WeatherServiceException(response.getBody().toString());
			else
				throw new WeatherServiceException("Response is null");
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
		weather.setWeatherServiceName(SERVICE_NAME);
		weather.setLocation(loc);
		weather.setWeatherServiceName(this.getClass().getSimpleName());
		
		return weather;
	}
}
