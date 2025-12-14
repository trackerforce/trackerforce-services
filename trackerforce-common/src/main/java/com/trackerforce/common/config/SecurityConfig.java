package com.trackerforce.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationResult;
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

	protected AuthorizationResult authorize(Supplier<? extends Authentication> supplier, RequestAuthorizationContext requestAuthorizationContext) {
		final var authentication = supplier.get();

		if (authentication == null || !authentication.isAuthenticated()) {
			return new AuthorizationDecision(false);
		}

		final var remoteAddress = requestAuthorizationContext.getRequest().getRemoteAddr();

		boolean isAllowed = false;
		for (String address : allowedAddresses) {
			IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(address);
			if (ipAddressMatcher.matches(remoteAddress)) {
				isAllowed = true;
				break;
			}
		}

		if (!isAllowed) {
			return new AuthorizationDecision(false);
		}

		return new AuthorizationDecision(true);
	}
}
