package com.trackerforce.common.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trackerforce.common.service.JwtTokenService;

public abstract class JwtRequestFilter extends OncePerRequestFilter {
	
	public static final String AUTHORIZATION = "Authorization";

	public static final String BEARER = "Bearer ";
	
	@Autowired
	protected JwtTokenService jwtTokenService;
	
	public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
		final String bearerToken = request.getHeader(AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
			return Optional.of(bearerToken.substring(7));
		}
		return Optional.empty();
	}

}