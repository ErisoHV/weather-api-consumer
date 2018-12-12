package com.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weather.model.Location;
import com.weather.services.apixu.ApixuService;
import com.weather.services.apixu.ApixuService.Temperature;
import com.weather.services.apixu.ApixuService.Volume;
import com.weather.services.darksky.DarkSkyService;
import com.weather.services.language.Language;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Value("${weather.language}")
	String aa;
	
	//@Autowired
	//AccuWeatherService test;
	
	public static void main(String[] args) {
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

		DarkSkyService test2 = new DarkSkyService().setKey("f53dd5b01075290f090f995f2d514964").setLanguage(Language.es).build();
		System.out.println(test2.getWeather(site));
	}
}
