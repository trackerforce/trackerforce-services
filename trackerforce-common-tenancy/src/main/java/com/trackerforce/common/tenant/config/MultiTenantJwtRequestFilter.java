package com.trackerforce.common.tenant.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.trackerforce.common.config.JwtRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class MultiTenantJwtRequestFilter extends JwtRequestFilter {

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		final Optional<String> jwt = getJwtFromRequest(request);
		try {
            jwt.ifPresent(token -> {
                String username = jwtTokenService.getUsernameFromToken(token);
                if (Boolean.TRUE.equals(jwtTokenService.validateToken(token, username))) {
                    final var authUser = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    authUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authUser);
                }
            });
		} catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException | SignatureException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		filterChain.doFilter(request, response);
	}

}