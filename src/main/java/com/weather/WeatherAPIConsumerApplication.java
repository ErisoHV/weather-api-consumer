package com.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherAPIConsumerApplication {

	@Value("${weather.language}")
	String aa;
	
	//@Autowired
	//AccuWeatherService test;
	
	public static void main(String[] args) {
		SpringApplication.run(WeatherAPIConsumerApplication.class, args);
	}
	
//	@Override
//	public void run(String... strings) throws Exception {
//		Location site = new Location();
//		site.setLongitude(6.27780191817659);
//		site.setLatitude(-75.6373785784056);
//		site.setServiceKey("107060");  // Example for Accuweather: 107060
//									   // Example for OpenWeather: 519188
//		site.setName("Novinki");
//
//		DarkSkyService test2 = new DarkSkyService().setKey("f53dd5b01075290f090f995f2d514964").setLanguage(Language.es).build();
//		System.out.println(test2.getWeather(site));
//	}
}
