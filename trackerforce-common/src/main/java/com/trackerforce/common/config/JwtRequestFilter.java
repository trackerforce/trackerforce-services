package com.trackerforce.common.config;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trackerforce.common.model.type.RequestHeader;
import com.trackerforce.common.service.JwtTokenService;

public abstract class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	protected JwtTokenService jwtTokenService;
	
	public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
		var bearerToken = request.getHeader(RequestHeader.AUTHORIZATION.toString());
		
		if (StringUtils.hasText(bearerToken) && 
				bearerToken.startsWith(RequestHeader.BEARER.toString()))
			return Optional.of(bearerToken.substring(7));
			
		return Optional.empty();
	}

}