package com.trackerforce.management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.trackerforce")
public class TrackerforceManagementApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
	}
	
}
