package com.trackerforce.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

public abstract class SecurityConfig {
	
	@Value("${service.endpoint.allowed-addresses}")
	protected String[] allowedAddresses;
	
	@Value("${service.endpoint.allowed-endpoints}")
	protected String[] allowedEndpoint;
	
	@Autowired
	protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	protected JwtRequestFilter jwtRequestFilter;

	protected AuthorizationDecision buildAllowedIpList() {
		AuthorizationDecision decision = new AuthorizationDecision(true);
		for (String address : allowedAddresses) {
			IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(address);
			if (!ipAddressMatcher.matches(address)) {
				decision = new AuthorizationDecision(false);
				break;
			}
		}
		return decision;
	}

}
