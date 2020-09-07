package com.trackerforce.identity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.trackerforce.common.config.SecurityConfig;

@SpringBootApplication
@ComponentScan(basePackages = { "com.trackerforce" }, excludeFilters = {
		  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
public class TrackerforceIdentityApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackerforceIdentityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
	}
	
}
