package com.trackerforce.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.trackerforce")
public class TrackerforceManagementApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceManagementApplication.class, args);
	}
	
}
