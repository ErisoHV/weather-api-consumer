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

import com.weather.exception.LocationNotFoundException;
import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.model.WeatherRequest;
import com.weather.services.core.WeatherService;
import com.weather.services.core.interfaces.LocationData;
import com.weather.utils.RequestUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class AccuWeatherService extends WeatherService implements LocationData{
	public static final String GEOPOSITION_URL = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search";
	public static final String SEARCHTEXT_URL = "http://dataservice.accuweather.com/locations/v1/search";
	public static final String WEATHER_URL = "http://dataservice.accuweather.com/currentconditions/v1/";
	public static final String APIPARAM_NAME = "apikey";
	
	public static final String SERVICE_NAME = "ACCUWEATHER";
	
	public enum fields {
		LocalizedName,
		Metric,
		EnglishName,
		EpochTime,
		AdministrativeArea,
		Country,
		Region,
		Key,
		Type,
		GeoPosition,
		Longitude,
		Latitude,
		WeatherText,
		WeatherIcon,
		Temperature,
		Value,
		Wind,
		Direction,
		Localized,
		Speed,
		PrecipitationSummary,
		Precipitation,
		RealFeelTemperature
	}
	

	@Override
	public List<Location> getLocationsDataByName(WeatherRequest request) {
		if (!RequestUtils.isOKWeatherKeyRequest(request))
			throw new WeatherServiceKeyException();
		
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", request.getLocation().getName());
		params.put("details", "false");
		params.put("language", request.getLanguage().toString());
		
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
			throw new WeatherServiceException(WeatherServiceException.buildErrorResponse(response));
		}
		return locations;
	}

	@Override
	public Location getLocationDataByGeoposition(WeatherRequest request) {
		if (!RequestUtils.isOKWeatherKeyRequest(request))
			throw new WeatherServiceKeyException();
		
		Map<String, String> params = new LinkedHashMap<>();
		params.put("q", RequestUtils.getCommaSeparatedLocationFromRequest(request));
		params.put("details", "false");
		if (request.getLanguage() != null)
			params.put("language", request.getLanguage().toString());
		setApiParamName(APIPARAM_NAME);
		setApiKey(request.getKey());
		ResponseEntity<Map> response 
				= getAPIWeatherResponseEntityMap(GEOPOSITION_URL, params);
		return buildResponseToLocation(response, (LocationData) this);
	}

	@Override
	public Location responseToLocation(Map<String, Object> element){
		
		if (element == null)
			throw new LocationNotFoundException();
		
		Location loc = new Location();
		loc.setName((String) element.get(fields.EnglishName.toString()));
		loc.setLocalizedName((String) element.get(fields.LocalizedName.toString()));
		Map<String, Object> subElement = (Map<String, Object>) element.get(fields.AdministrativeArea.toString());
		if (subElement != null && !subElement.isEmpty()){
			loc.setAdministrativeAreaName((String) subElement.get(fields.LocalizedName.toString()));
		}
		subElement = (Map<String, Object>) element.get(fields.Country.toString());
		if (subElement != null && !subElement.isEmpty()){
			loc.setLocationCountryName((String) subElement.get(fields.LocalizedName.toString()));
		}
		subElement = (Map<String, Object>) element.get(fields.Region.toString());
		if (subElement != null && !subElement.isEmpty()){
			loc.setLocationRegionName((String) subElement.get(fields.LocalizedName.toString()));
		}
		loc.setServiceKey((String) element.get(fields.Key.toString()));
		loc.setLocationType((String) element.get(fields.Type.toString()));
		
		subElement = (Map<String, Object>) element.get(fields.GeoPosition.toString());
		if (subElement != null && !subElement.isEmpty()){
			loc.setLongitude((double) subElement.get(fields.Longitude.toString()));
			loc.setLatitude((double) subElement.get(fields.Latitude.toString()));
		}
		return loc;
	}

	@Override
	public CurrentWeatherStatus getWeather(WeatherRequest request) {
		if (!RequestUtils.isOKWeatherKeyRequest(request))
			throw new WeatherServiceKeyException();
		
		Map<String, String> params = new HashMap<>();
		params.put("details", "true");
		if (request.getLanguage() != null)
			params.put("language", request.getLanguage().toString());
		ResponseEntity<List> response = getAPIWeatherResponseEntityList(WEATHER_URL 
				+ request.getLocation().getServiceKey(), params);
		if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
			List<Map<String, Object>> body = response.getBody();
			if (body != null && !body.isEmpty()){
				return responseToWeather(body.get(0), request.getLocation());
			}
			
		} else{
			throw new WeatherServiceException(WeatherServiceException.buildErrorResponse(response));
		}
		
		return null;
	}
	
	private String getWeatherIcon(String iconId) {
		if (iconId != null && !iconId.isEmpty()) {
			if (iconId.length() == 1) {
				return "0" + iconId + "-s.png";
			} else {
				return iconId + "-s.png";
			}
		}
		return "";
	}

	@Override
	protected CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = buildWeatherStatus(element, loc);

		setTemperatureFromElement(weather, element);
		
		setWindFromElement(weather, element);
		
		setPrecipitationFromElement(weather, element);
		
		return weather;
	}
	
	private CurrentWeatherStatus buildWeatherStatus(Map<String, Object> element, Location loc) {
		CurrentWeatherStatus weather = new CurrentWeatherStatus();
		weather.setEpochTime(new Timestamp(Long.valueOf((Integer) element.get(fields.EpochTime.toString()))));
		weather.setWeatherDescription((String) element.get(fields.WeatherText.toString()));
		
		String icon = String.valueOf((Integer) element.get(fields.WeatherIcon.toString()));
		weather.setWeatherIcon(getWeatherIcon(icon));
		
		weather.setWeatherServiceName(SERVICE_NAME);
		weather.setWeatherServiceName(this.getClass().getSimpleName());
		weather.setLocation(loc);
		
		return weather;
	}
	
	private void setTemperatureFromElement(CurrentWeatherStatus weather, Map<String, Object> element) {
		Map<String, Map<String, Object>> subElement = 
				(Map<String, Map<String, Object>>) element.get(fields.Temperature.toString());
		if (subElement != null && subElement.get(fields.Metric.toString())  != null){
			weather.setTemperature((double) subElement.get(fields.Metric.toString()).get(fields.Value.toString()));
		}
		subElement = (Map<String, Map<String, Object>>) element.get(fields.RealFeelTemperature.toString());
		if (subElement != null && subElement.get(fields.Metric.toString()) != null){
			weather.setRealFeelTemperature((double) subElement.get(fields.Metric.toString()).get(fields.Value.toString()));
		}
	}
	
	private void setWindFromElement (CurrentWeatherStatus weather, Map<String, Object> element) {
		Map<String, Map<String, Object>> subElement 
				= (Map<String, Map<String, Object>>) element.get(fields.Wind.toString());
		if (subElement != null){
			if (subElement.get(fields.Direction.toString()) != null){
				weather.setWindDirection((String) subElement.get(fields.Direction.toString()).get(fields.Localized.toString()));
			}
			if (subElement.get(fields.Speed.toString()) != null 
					&& subElement.get(fields.Speed.toString()).get(fields.Metric.toString()) != null){
				Map<String, Object> subSubElement = 
						(Map<String, Object>) subElement.get(fields.Speed.toString()).get(fields.Metric.toString());
				weather.setWindSpeed((double) subSubElement.get(fields.Value.toString()));
			}
		}
	}
	
	private void setPrecipitationFromElement(CurrentWeatherStatus weather, Map<String, Object> element) {
		Map<String, Map<String, Object>> subElement 
				= (Map<String, Map<String, Object>>) element.get(fields.PrecipitationSummary.toString());
		if (subElement != null){
			if (subElement != null && subElement.get(fields.Precipitation.toString()) != null){
				 Map<String, Object> subSubElement =
						 (Map<String, Object>) subElement.get(fields.Precipitation.toString()).get(fields.Metric.toString());
				 weather.setPrecipitation((double) subSubElement.get(fields.Value.toString()));
			}
		}
	}
}
