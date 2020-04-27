package com.weather.services.apixu;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.exception.WeatherIlegalParameterException;
import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.core.WeatherService;
import com.weather.services.core.common.language.Language;
import com.weather.services.core.interfaces.LocationData;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ApixuService extends WeatherService implements LocationData{
	public static final String SEARCHTEXT_URL = "http://api.apixu.com/v1/search.json";
	public static final String WEATHER_URL = "http://api.apixu.com/v1/current.json";
	public static final String APIPARAM_NAME = "key";
	
	public static final String SERVICE_NAME = "APIXU";
	
	private Temperature tempUnit;
	private Volume precipUnit;
	private Velocity windUnit;
	
	public enum Temperature {
		/**
		 * Temperature constant for Celcius = c
		 */
		c,
		/**
		 * Temperature constant for Fahrenheit = f
		 */
		f
	}
	
	public enum Velocity {
		/**
		 * Meters per hour constant = mph
		 */
		mph,
		
		/**
		 * Kilometers per hour constant = kph
		 */
		kph
	}

	public enum Volume {
		/**
		 * Milimetre constant = mm
		 */
		mm,
		
		/**
		 * Milimetre constant = mm
		 */
		in
	}

	private ApixuService(ApixuService apixu){
		super(apixu.getApiKey(), APIPARAM_NAME);
		setByApiQueryParam(true);
		setApiLanguage(apixu.getApiLanguage());
		setTempUnit(apixu.getTempUnit());
		setWindUnit(apixu.getWindUnit());
		setPrecipUnit(apixu.getPrecipUnit());
	}
	
	public ApixuService() {
		setApiLanguage(Language.en); 	// default language
		setTempUnit(Temperature.c);	  	// default temperature
		setWindUnit(Velocity.kph); 		// default wind velocity
		setPrecipUnit(Volume.mm);		// default volume 
	}
	
	public ApixuService setKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}
	
	public ApixuService setLanguage(Language lang){
		if (lang == null || (lang != null && Language.parse(lang.toString()) == null)) {
			throw new WeatherIlegalParameterException("languaje", Language.getValuesArray());
		}
			
		setApiLanguage(lang);
		return this;
	}
	
	public Temperature getTempUnit() {
		return tempUnit;
	}

	public ApixuService setTempUnit(Temperature tempUnit) {
		if (tempUnit == null){
			throw new IllegalArgumentException("Values for temperature are: " + Temperature.c + " (celcius) or " 
					+ Temperature.f + " (fahrenheit)");
		}
		this.tempUnit = tempUnit;
		return this;
	}

	public Volume getPrecipUnit() {
		return precipUnit;
	}

	public ApixuService setPrecipUnit(Volume precipUnit) {
		if (precipUnit == null){
			throw new IllegalArgumentException("Values for Precipitation are: " + Volume.mm 
					+ " (milimetre) or " + Volume.in + " (inch)");
		}
		this.precipUnit = precipUnit;
		return this;
	}

	public Velocity getWindUnit() {
		return windUnit;
	}

	public ApixuService setWindUnit(Velocity windUnit) {
		if (windUnit == null){
			throw new IllegalArgumentException("Values for Wind Velocity are: " + Velocity.mph + " (meters per hour) or " 
					+ Velocity.kph + " (kilometers per hour)");
		}
		this.windUnit = windUnit;
		return this;
	}

	public ApixuService build(){
		if (!isValidKey())
			throw new WeatherServiceKeyException();
		return new ApixuService(this);
	}

	@Override
	public List<Location> getLocationsDataByName(String siteName) {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", siteName);
		params.put("lang", getApiLanguage().toString());
		return getLocations(params);
	}

	@Override
	public Location getLocationDataByGeoposition(double lat, double lon) {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", lat + "," + lon);
		ArrayList<Location> list= (ArrayList<Location>) getLocations(params);
		if (!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	private List<Location> getLocations(Map<String, String> params){
		params.put("lang", getApiLanguage().toString());
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
			throw new WeatherServiceException(response);
		}
		return locations;
	}
	
	public CurrentWeatherStatus getWeatherByName(String name){
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("The location name cannot be null or empty");
		
		Location loc = new Location();
		loc.setName(name);
		return getWeather(loc);
	}
	
	public CurrentWeatherStatus getWeatherByGeoposition(double lat, double lon){
		Location loc = new Location();
		loc.setLatitude(lat);
		loc.setLongitude(lon);
		Map<String, String> params = new HashMap<>();
		params.put("q", loc.getLatitude() + ", " + loc.getLongitude());
		params.put("lang", getApiLanguage().toString());

		return callToApixuWeatherService(params, loc);
	}

	/**
	 * Get the weather by location name
	 * @param site The location with a defined name
	 * @return  CurrentWeatherStatus
	 */
	@Override
	public CurrentWeatherStatus getWeather(Location site) {
		Map<String, String> params = new HashMap<>();
		params.put("q", site.getName());
		params.put("lang", getApiLanguage().toString());

		return callToApixuWeatherService(params,site);
	}
	
	private CurrentWeatherStatus callToApixuWeatherService(Map<String, String> params, Location site){
		ResponseEntity<Map> response = getAPIWeatherResponseEntityMap(WEATHER_URL, params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			Map<String, Object> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body, site);
			}
		} else{
			 throw new WeatherServiceException(response);
		}
		return null;
	}

	@Override
	public Location responseToLocation(Map<String, Object> element) {
		Location loc = new Location();
		loc.setServiceKey(String.valueOf(element.get("id")));
		loc.setName((String) element.get("name"));
		loc.setLocationRegionName((String) element.get("region"));
		loc.setLocationCountryName((String) element.get("country"));
		
		loc.setLatitude((double) element.get("lat"));
		loc.setLongitude((double) element.get("lon"));

		return loc;
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(
			Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		Map<String, Object> subElement = (Map<String, Object>) element.get("current");
		if (subElement != null){
			weather.setEpochTime(new Timestamp(Long.valueOf((Integer) subElement.get("last_updated_epoch"))));
			weather.setTemperature((double) subElement.get("temp_" + getTempUnit()));
			weather.setWindSpeed((double) subElement.get("wind_" + getWindUnit()));
			weather.setWindDirection((String) subElement.get("wind_dir"));
			weather.setPrecipitation((double) subElement.get("precip_" + getPrecipUnit()));
			weather.setRealFeelTemperature((double) subElement.get("feelslike_" + getTempUnit()));
			
			Map<String, Object> subSubElement = 
					(Map<String, Object>) subElement.get("condition");
			if (subSubElement != null){
				weather.setWeatherDescription((String) subSubElement.get("text"));
				weather.setWeatherIcon((String) subSubElement.get("icon"));
			}
		}
		
		subElement = (Map<String, Object>) element.get("location");
		if (subElement != null){
			loc = responseToLocation(subElement);
		}
		weather.setWeatherServiceName(SERVICE_NAME);
		weather.setLocation(loc);
		return weather;
	}
	
}
