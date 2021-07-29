package com.trackerforce.common.config;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public abstract class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${service.endpoint.allowed-addresses}")
	protected String allowedAddresses;
	
	@Value("${service.endpoint.allowed-endpoints}")
	protected String allowedEndpoint;
	
	@Autowired
	protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	protected JwtRequestFilter jwtRequestFilter;
	
	protected String buildAllowedIpList() {
		final String[] addresses = allowedAddresses.split(",");
		final String accessIpAddress = Arrays.stream(addresses)
				.map(address -> "hasIpAddress('" + address.trim() + "') or ")
				.collect(Collectors.joining());

		return accessIpAddress.substring(0, accessIpAddress.length() - 4);
	}

}
