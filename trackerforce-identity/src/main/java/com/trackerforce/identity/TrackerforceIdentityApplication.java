package com.trackerforce.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = { "com.trackerforce" })
@ComponentScan(basePackages = { "com.trackerforce" })
public class TrackerforceIdentityApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceIdentityApplication.class, args);
	}
	
}
