package com.trackerforce.identity.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.config.JwtRequestFilter;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.AccessRequest;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.repository.AuthAccessRepository;

@Service
public class AuthenticationService extends AbstractIdentityService<AuthAccess> {
	
	public static final String ACCESS = "access";
	
	public static final String TOKEN = "token";
	
	public static final String REFRESH_TOKEN = "refreshToken";
	
	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUserDetailsService userDetailsService;
	
	@Autowired
	AuthAccessRepository authAccessRepository;
	
	@Autowired
	JwtTokenService jwtTokenUtil;
	
	public Map<String, Object> authenticateAccess(JwtRequest authRequest) {
		authenticate(authRequest);
		
		final var authAccess = authAccessRepository.findByUsername(authRequest.getUsername());
		final var jwt = jwtTokenUtil.generateToken(authAccess.getUsername());
		
		Map<String, Object> response = new HashMap<>();
		response.put(ACCESS, authAccess);
		response.put(TOKEN, jwt[0]);
		response.put(REFRESH_TOKEN, jwt[1]);
		
		authAccess.setTokenHash(bcrypt.encode(jwt[0]));
		authAccessRepository.save(authAccess);
		
		return response;
	}
	
	public AuthAccess getAuthenticated(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var authAccess = authAccessRepository.findByUsername(authentication.getName());
		
		Optional<String> token = JwtRequestFilter.getJwtFromRequest(request);
		if (!token.isPresent() || !bcrypt.matches(token.get(), authAccess.getTokenHash()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		return authAccess;
	}
	
	public AuthAccess registerAccess(AccessRequest accessRequest) {
		var authAccess = accessRequest.getAuthAccess();
		validate(authAccess);
		return userDetailsService.save(authAccess);
	}
	
	public void logoff(HttpServletRequest request, HttpServletResponse response) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            final var authAccess = authAccessRepository.findByUsername(authentication.getName());
            authAccess.setTokenHash(null);
    		authAccessRepository.save(authAccess);
    		
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
	}

	@Override
	protected void validate(AuthAccess entity) {
		if (!StringUtils.hasText(entity.getUsername()) || 
				!StringUtils.hasText(entity.getPassword()))
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Invalid AuthAccess values");
	}
	
	private void authenticate(JwtRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (DisabledException e) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED, "USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
		}
	}

}
