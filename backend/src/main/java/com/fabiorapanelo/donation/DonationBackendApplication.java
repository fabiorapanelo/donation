package com.fabiorapanelo.donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;

@SpringBootApplication
public class DonationBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationBackendApplication.class, args);
	}
	
	@Bean
    public GeoJsonModule geoJsonModule(){
        return new GeoJsonModule();
    }
	
	@Bean
	public EncryptPasswordListener encryptPasswordListener(){
		return new EncryptPasswordListener();
	}
}
