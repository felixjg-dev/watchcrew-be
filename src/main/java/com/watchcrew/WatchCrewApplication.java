package com.watchcrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WatchCrewApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchCrewApplication.class, args);
	}

}
