package com.trackerforce.identity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.trackerforce" })
public class TrackerforceIdentityApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceIdentityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
	}
	
}
