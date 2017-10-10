package com.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weather.model.Location;
import com.weather.services.WeatherService;
import com.weather.services.accuweather.AccuWeatherService;
import com.weather.services.language.Language;

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
		site.setServiceKey("107060");  // Example for Accuweather: 107060
									   // Example for OpenWeather: 519188
		site.setName("Novinki");
		
//		WeatherService test = new OpenWeatherService().setKey("8662a8d6fca3bb23af6c4942004be03a").setLanguage(Language.az).build();
//		System.out.println(test.getWeather(site));
//		Location a = test.getLocationDataByGeoposition(6.27780191817659,-75.6373785784056);
//		System.out.println(a);
//		System.out.println(test.getWeather(a));
//rLMgHjivBUnDHqOvZvr4eB52fxmYrAaP
		WeatherService test = new AccuWeatherService().setKey("rLMgHjivBUnDHqOvZvr4eB52fxmYrAaP").setLanguage(Language.es).build();
		System.out.println(test.getWeather(site));
		
//		ApixuService test = new ApixuService().setKey("1c4f43ac14484c25895163008170210")
//				.setLanguage(Language.az).setTempUnit(Temperature.f).setPrecipUnit(Volume.mm).build();
//		System.out.println(test.getLocationDataByGeoposition(48.8567, 2.3508));
		
		
	}
}
