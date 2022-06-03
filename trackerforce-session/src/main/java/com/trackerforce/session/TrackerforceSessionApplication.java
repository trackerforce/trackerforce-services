package com.trackerforce.session;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.trackerforce.session.config.Features.*;

@SpringBootApplication(scanBasePackages = "com.trackerforce")
public class TrackerforceSessionApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceSessionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		checkSwitchers();
	}
	
}
