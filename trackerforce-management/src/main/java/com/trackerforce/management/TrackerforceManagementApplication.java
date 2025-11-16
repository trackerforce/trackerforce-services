package com.trackerforce.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = { "com.trackerforce" })
@ComponentScan(basePackages = { "com.trackerforce" })
public class TrackerforceManagementApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceManagementApplication.class, args);
	}
	
}
