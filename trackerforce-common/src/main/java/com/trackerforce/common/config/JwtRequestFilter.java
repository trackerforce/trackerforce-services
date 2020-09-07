package com.trackerforce.common.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trackerforce.common.service.JwtTokenService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	public static final String AUTHORIZATION = "Authorization";

	public static final String BEARER = "Bearer ";
	
	@Autowired
	private JwtTokenService jwtTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final Optional<String> jwt = getJwtFromRequest(request);
		jwt.ifPresent(token -> {
			try {
				String username = jwtTokenService.getUsernameFromToken(token);
				if (jwtTokenService.validateToken(token, username)) {
					final UsernamePasswordAuthenticationToken authUser = 
							new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
					authUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authUser);
				}
			} catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException | SignatureException e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		});
		
		filterChain.doFilter(request, response);
	}
	
	private static Optional<String> getJwtFromRequest(HttpServletRequest request) {
		final String bearerToken = request.getHeader(AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
			return Optional.of(bearerToken.substring(7));
		}
		return Optional.empty();
	}

}