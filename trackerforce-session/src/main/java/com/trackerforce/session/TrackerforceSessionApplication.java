package com.trackerforce.session;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

import static com.trackerforce.session.config.Features.*;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = { "com.trackerforce" })
@ComponentScan(basePackages = { "com.trackerforce" })
public class TrackerforceSessionApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceSessionApplication.class, args);
	}

	@Override
	public void run(String... args) {
		checkSwitchers();
	}
	
}
