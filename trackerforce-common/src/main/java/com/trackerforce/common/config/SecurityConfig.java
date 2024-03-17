package com.trackerforce.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.function.Supplier;

public abstract class SecurityConfig {
	
	@Value("${service.endpoint.allowed-addresses}")
	protected String[] allowedAddresses;
	
	@Value("${service.endpoint.allowed-endpoints}")
	protected String[] allowedEndpoint;
	
	@Autowired
	protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	protected JwtRequestFilter jwtRequestFilter;

	protected AuthorizationDecision authorize(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		final var remoteAddress = object.getRequest().getRemoteAddr();
		var decision = new AuthorizationDecision(authentication.get().isAuthenticated());

		boolean isAllowed = false;
		for (String address : allowedAddresses) {
			IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(address);
			if (ipAddressMatcher.matches(remoteAddress)) {
				isAllowed = true;
				break;
			}
		}

		if (!isAllowed) {
			decision = new AuthorizationDecision(false);
		}

		return decision;
	}

}
