package com.trackerforce.identity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.config.JwtRequestFilter;
import com.trackerforce.common.config.RequestHeader;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.AccessRequest;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.repository.AuthAccessRepository;

import io.jsonwebtoken.Claims;

@Service
public class AuthenticationService extends AbstractIdentityService<AuthAccess> {
	
	public static final String ACCESS = "access";
	
	public static final String TOKEN = "token";
	
	public static final String REFRESH_TOKEN = "refreshToken";
	
	private final BCryptPasswordEncoder bcrypt;
	
	private final AuthenticationManager authenticationManager;
	
	private final AuthAccessRepository authAccessRepository;
	
	private final JwtUserDetailsService userDetailsService;
	
	private final JwtTokenService jwtTokenUtil;
	
	public AuthenticationService(
			AuthenticationManager authenticationManager,
			AuthAccessRepository authAccessRepository,
			JwtUserDetailsService userDetailsService,
			JwtTokenService jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.authAccessRepository = authAccessRepository;
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.bcrypt = new BCryptPasswordEncoder();
	}
	
	public Map<String, Object> authenticateAccess(JwtRequest authRequest) {
		authenticate(authRequest);
		
		final var authAccess = authAccessRepository.findByUsername(authRequest.getUsername());
		final var jwt = jwtTokenUtil.generateToken(authAccess.getUsername(), 
				authAccess.getOrganization().getAlias(), authAccess.getDefaultClaims());
		
		var response = new HashMap<String, Object>();
		response.put(ACCESS, authAccess);
		response.put(TOKEN, jwt[0]);
		response.put(REFRESH_TOKEN, jwt[1]);
		
		authAccess.setTokenHash(bcrypt.encode(jwt[0]));
		authAccessRepository.save(authAccess);
		
		return response;
	}
	
	public Object getAuthenticated(HttpServletRequest request) throws ServiceException {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		
		var token = JwtRequestFilter.getJwtFromRequest(request);
		if (!token.isPresent())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		var roles = jwtTokenUtil.getClaimFromToken(
				token.get(), claims -> claims.get(JwtTokenService.ROLES, List.class));
		
		if (roles.contains("ROOT")) {
			return getRootAuthenticated(request, authentication, token.get());
		} else if (roles.contains("AGENT") || roles.contains("SESSION")) {
			return getInternalAuthenticated(request, authentication, token.get());
		}
		
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}
	
	public AuthAccess getRootAuthenticated(HttpServletRequest request, 
			Authentication authentication, String token) {
		var authAccess = authAccessRepository.findByUsername(authentication.getName());
		if (authAccess == null || !bcrypt.matches(token, authAccess.getTokenHash()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);
		if (!StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		return authAccess;
	}
	
	public AuthAccess getInternalAuthenticated(HttpServletRequest request, 
			Authentication authentication, String token) {
		
		if (!authentication.isAuthenticated())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);
		if (!StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		return new AuthAccess(jwtTokenUtil.getUsernameFromToken(token), orgAlias);
	}
	
	public AuthAccess registerAccess(AccessRequest accessRequest) {
		var authAccess = accessRequest.getAuthAccess();
		this.validate(authAccess);
		
		return userDetailsService.newUser(authAccess);
	}
	
	public void logoff(HttpServletRequest request, HttpServletResponse response) 
			throws ServiceException {
        final AuthAccess authAccess = (AuthAccess) getAuthenticated(request);
        authAccess.setTokenHash(null);
		authAccessRepository.save(authAccess);
		
        new SecurityContextLogoutHandler().logout(
        		request, response, SecurityContextHolder.getContext().getAuthentication());
	}

	@Override
	protected void validate(AuthAccess entity) {
		if  (!entity.hasValidOrganization())
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Invalid Organization values");
		
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
