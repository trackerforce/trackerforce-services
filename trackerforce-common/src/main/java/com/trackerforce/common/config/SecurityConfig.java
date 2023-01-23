package com.trackerforce.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class SecurityConfig {
	
	@Value("${service.endpoint.allowed-addresses}")
	protected String[] allowedAddresses;
	
	@Value("${service.endpoint.allowed-endpoints}")
	protected String[] allowedEndpoint;
	
	@Autowired
	protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	protected JwtRequestFilter jwtRequestFilter;
	
	protected String buildAllowedIpList() {
		final String accessIpAddress = Arrays.stream(allowedAddresses)
				.map(address -> "hasIpAddress('" + address.trim() + "') or ")
				.collect(Collectors.joining());

		return accessIpAddress.substring(0, accessIpAddress.length() - 4);
	}

}
