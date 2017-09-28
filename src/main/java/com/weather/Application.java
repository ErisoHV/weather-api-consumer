package com.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weather.model.Location;
import com.weather.services.WeatherService;
import com.weather.services.openweather.OpenWeatherService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Value("${weather.language}")
	String aa;
	
	//@Autowired
	//AccuWeatherService test;
	
	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {
		Location site = new Location();
		site.setLongitude(6.27780191817659);
		site.setLatitude(-75.6373785784056);
		site.setServiceKey("4607005");
		
		WeatherService test = new OpenWeatherService().setKey("8662a8d6fca3bb23af6c4942004be03a").build();
		System.out.println(test.getWeather(site));
//		Location a = test.getLocationDataByGeoposition(6.27780191817659,-75.6373785784056);
//		System.out.println(a);
//		System.out.println(test.getWeather(a));
		
		
		
		
//		test = new AccuWeatherService().setKey("rLMgHjivBUnDHqOvZvr4eB52fxmYrAaP").build();
//		System.out.println(test.getWeather(site));
		
//		test = new DarkSkyService().setKey("f53dd5b01075290f090f995f2d514964").build();
//
//		System.out.println(test.getWeather(site));
	}
	
}
