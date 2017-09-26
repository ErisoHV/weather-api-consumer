package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weather.properties.AccuWeatherConfigProperties;
import com.weather.services.AccuWeatherService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Value("${weather.language}")
	String aa;
	
	@Autowired
	AccuWeatherConfigProperties conf ;
	
	@Autowired
	AccuWeatherService test;
	
	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {

//		String url = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search";
//		Map<String, String> uriParams = new HashMap<String, String>();
//		uriParams.put("apikey", "");
//		uriParams.put("q", "6.27780191817659,-75.6373785784056");
//		
//		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//				.queryParam("apikey", "rLMgHjivBUnDHqOvZvr4eB52fxmYrAaP") //rLMgHjivBUnDHqOvZvr4eB52fxmYrAaP
//				.queryParam("q", "6.27780191817659,-75.6373785784056")
//				.queryParam("language", "es")
//				.queryParam("details", false); //6.27780191817659,-75.6373785784056
//		
//		RestTemplate template  = new RestTemplate();
//		//template.exchange(builder.toUriString(), HttpMethod.GET, RequestEntity.EMPTY, Map.class);
//		try {
//			ResponseEntity<Map> map = template.exchange(builder.toUriString(), HttpMethod.GET, RequestEntity.EMPTY, Map.class);
//			if (map.getStatusCode().equals(HttpStatus.OK)){
//				
//				System.out.println(map.getBody());
//				
//				Map<String, Object> awGeoPositionInfo = map.getBody();
//				if (awGeoPositionInfo != null){
//					System.out.println("AccuWeather key = " + awGeoPositionInfo.get("Key"));
//					System.out.println("Name            = " + awGeoPositionInfo.get("LocalizedName"));
//					
//					Map<String, String> region = (Map<String, String>) awGeoPositionInfo.get("Region");
//					System.out.println("Region          = " + region.get("LocalizedName"));
//					
//					Map<String, String> country = (Map<String, String>) awGeoPositionInfo.get("Country");
//					System.out.println("Country         = " + country.get("LocalizedName"));
//					
//					Map<String, String> area = (Map<String, String>) awGeoPositionInfo.get("AdministrativeArea");
//					System.out.println("Area            = " + area.get("LocalizedName"));
//					System.out.println("Area Type       = " + area.get("LocalizedType"));
//					
//					Map<String, Double> geo = (Map<String, Double>) awGeoPositionInfo.get("GeoPosition");
//					System.out.println("Lat             = " + geo.get("Latitude"));
//					System.out.println("Lat             = " + geo.get("Longitude"));
//					
//				}
//				else{
//					System.err.println("Location not found");
//				}
//			}
//			
//
//		} catch (RestClientException e) {
//			System.err.println("Rest Client exception, check API key and query params. Response = " + e.getMessage());
//		}

		
		test.getLocationDataByName("Medellin,Antioquia");
	
	}
	
}
