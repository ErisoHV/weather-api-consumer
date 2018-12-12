package com.weather.services.core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.exception.WeatherServiceException;
import com.weather.exception.WeatherServiceKeyException;
import com.weather.model.CurrentWeatherStatus;
import com.weather.model.Location;
import com.weather.services.language.Language;

public abstract class WeatherService {
	private String apiKey;
	private String apiParamName;
	private boolean isByApiQueryParam = true;
	private Language apiLanguage;
	
	private  static final Logger LOGGER = Logger.getLogger(WeatherService.class);
	
	protected WeatherService(){
		
	}
	
	protected WeatherService(String apiKey, String apiParamName) {
		this.apiKey = apiKey;
		this.apiParamName = apiParamName;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	protected void setApiKey(String apiKey) {
		if (isValidKey())
		    throw new WeatherServiceKeyException();
		
		this.apiKey = apiKey;
	}
	
	public String getApiParamName() {
		return apiParamName;
	}

	protected void setApiParamName(String apiParamName) {
		this.apiParamName = apiParamName;
	}
	
	protected boolean isByApiQueryParam() {
		return isByApiQueryParam;
	}

	protected void setByApiQueryParam(boolean isByApiQueryParam) {
		this.isByApiQueryParam = isByApiQueryParam;
	}

	public Language getApiLanguage() {
		return apiLanguage;
	}

	protected void setApiLanguage(Language language) {
		if (language == null || language.toString().isEmpty())
			 throw new IllegalArgumentException("Language cannot be null or empty");
		this.apiLanguage = language;
	}

	protected boolean isValidKey(){
		return this.getApiKey() != null && !this.getApiKey().isEmpty();
	}

	protected boolean isValidParamKey(){
		return this.getApiParamName() != null && !this.getApiParamName().isEmpty();
	}

	protected void validateApiQueryParam(){
		if (!isValidKey()){
			throw new WeatherServiceKeyException();
		}
		if (isByApiQueryParam && !isValidParamKey()){	
				throw new IllegalArgumentException("paramKey name cannot be null or empty. Use build()");
		}

	}

	private void addURLParameters(UriComponentsBuilder builder, Map<String, String> params){
		if (params != null){
			for (Entry<String, String> entry : params.entrySet()){
				builder = builder.queryParam(entry.getKey() , entry.getValue());
			}
		}
	}
	
	private void addPathParameters(UriComponentsBuilder builder, List<String> params){
		if (params != null){
			for (String entry : params){
				builder = builder.path(entry).path("/");
			}
		}
	}
	
	private UriComponents buildURLParameters(String url, Map<String, String> urlParams, List<String> pathParams){
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		if (isByApiQueryParam){
			builder = builder.queryParam(apiParamName, apiKey);
		} else{
			builder = builder.path(apiKey).path("/");
		}
		addPathParameters(builder, pathParams);
		addURLParameters(builder, urlParams);
		return builder.build();
	}
	
	/**
	 * Return a generic ResponseEntity List from any service
	 * @param url Service API URL
	 * @param params Query parameters
	 * @return ResponseEntity List
	 */
	@SuppressWarnings("rawtypes")
	protected ResponseEntity<List> getAPIWeatherResponseEntityList(String url, Map<String, String> params){
		UriComponents urlBuilded = buildURLParameters(url, params, null);
		return getAPIWeatherResponseEntityList(urlBuilded);
	}
	
	/**
	 *Return a generic ResponseEntity List from any service
	 * @param url Service API URL
	 * @param urlParams Query parameters
	 * @param pathParams List
	 * @return ResponseEntity
	 */
	@SuppressWarnings("rawtypes")
	protected ResponseEntity<List> getAPIWeatherResponseEntityList(String url, Map<String, String> urlParams, List<String> pathParams){
		UriComponents urlBuilded = buildURLParameters(url, urlParams, pathParams);
		return getAPIWeatherResponseEntityList(urlBuilded);
	}
	
	@SuppressWarnings("rawtypes")
	protected ResponseEntity<List> getAPIWeatherResponseEntityList(String url, List<String> pathParams){
		UriComponents urlBuilded = buildURLParameters(url, null, pathParams);
		return getAPIWeatherResponseEntityList(urlBuilded);
	}
	
	private ResponseEntity<List> getAPIWeatherResponseEntityList(UriComponents urlBuilded){
		RestTemplate template  = new RestTemplate();
		try {
			ResponseEntity<List> map = template.getForEntity(urlBuilded.toUriString(), List.class);
				if (map.getStatusCode().equals(HttpStatus.OK)){
					return map;
				}
				else{
					LOGGER.error("[WeatherService -> getResponseEntityList] Error (" + map.getStatusCodeValue() + ") " +
							map.getBody());
					throw new WeatherServiceException("Error (" + map.getStatusCodeValue() + ") " + map.getBody());
				}
		} catch (RestClientException e) {
			throw new WeatherServiceException("Rest Client exception, check API key and query params. Response = " + e.getMessage());
		}
	}
	
	/**
	 * Return a generic ResponseEntity Map from any service
	 * @param url Service API URL
	 * @param params Query parameters
	 * @return ResponseEntity Map
	 */
	@SuppressWarnings("rawtypes")
	protected ResponseEntity<Map> getAPIWeatherResponseEntityMap(String url, Map<String, String> params){
		UriComponents urlBuilded = buildURLParameters(url, params, null);
		return getAPIWeatherResponseEntityMap(urlBuilded);
	}
	
	/**
	 * Return a generic ResponseEntity Map from any service
	 * @param url Service API URL
	 * @param params Query parameters
	 * @return ResponseEntity Map
	 */
	@SuppressWarnings("rawtypes")
	protected ResponseEntity<Map> getAPIWeatherResponseEntityMap(String url, Map<String, String> urlParams, List<String> pathParams){
		UriComponents urlBuilded = buildURLParameters(url, urlParams, pathParams);
		return getAPIWeatherResponseEntityMap(urlBuilded);
	}
	
	@SuppressWarnings("rawtypes")
	protected ResponseEntity<Map> getAPIWeatherResponseEntityMap(String url, List<String> pathParams){
		UriComponents urlBuilded = buildURLParameters(url, null, pathParams);
		return getAPIWeatherResponseEntityMap(urlBuilded);
	}
	
	private ResponseEntity<Map> getAPIWeatherResponseEntityMap(UriComponents urlBuilded){
		RestTemplate template  = new RestTemplate();
		try {
			ResponseEntity<Map> map = template.getForEntity(urlBuilded.toUriString(), Map.class);
				if (map.getStatusCode().equals(HttpStatus.OK)){
					return map;
				}
				else{
					LOGGER.error("[WeatherService -> getResponseEntityMap] Error (" + map.getStatusCodeValue() + ") " +
							map.getBody());
					throw new WeatherServiceException("Error (" + map.getStatusCodeValue() + ") " + map.getBody());
				}
		} catch (RestClientException e) {
			throw new WeatherServiceException("Rest Client exception, check API key and query params. Response = " + e.getMessage());
		}
	}

	public abstract CurrentWeatherStatus getWeather(Location site);
	
	protected abstract CurrentWeatherStatus responseToWeather(Map<String, Object> element, Location loc);
	
}
