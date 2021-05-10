package com.trackerforce.identity.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

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
		
		return response;
	}
	
	public AuthAccess registerAccess(AccessRequest accessRequest) {
		var authAccess = accessRequest.getAuthAccess();
		validate(authAccess);
		return userDetailsService.save(authAccess);
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
