package com.trackerforce.identity.config;

import com.trackerforce.common.config.JwtRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class IdentityJwtRequestFilter extends JwtRequestFilter {

	private final List<String> allowedPaths = List.of("/refresh");

	@Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {

		if (isPathAllowed(request.getServletPath())) {
			filterChain.doFilter(request, response);
			return;
		}

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

	private boolean isPathAllowed(String path) {
		for (String allowedPath : allowedPaths) {
			if (path.endsWith(allowedPath)) {
				return true;
			}
		}
		return false;
	}

}