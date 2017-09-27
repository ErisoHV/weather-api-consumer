package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weather.model.Location;
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

//		List<Location> locations = test.getLocationsDataByName("Medellin");
//		System.out.println(locations);
		Location a = test.getLocationDataByGeoposition(6.27780191817659,-75.6373785784056);
		System.out.println(a);
		System.out.println(test.getWeather(a));
	
	}
	
}
