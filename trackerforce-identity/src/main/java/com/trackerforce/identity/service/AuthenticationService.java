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
import com.trackerforce.common.model.type.JwtKeys;
import com.trackerforce.common.model.type.RequestHeader;
import com.trackerforce.common.model.type.ServicesRole;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.AccessRequest;
import com.trackerforce.identity.model.request.JwtRefreshRequest;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.repository.AuthAccessRepository;

import io.jsonwebtoken.Claims;

@Service
public class AuthenticationService extends AbstractIdentityService<AuthAccess> {

	private final BCryptPasswordEncoder bcrypt;

	private final AuthenticationManager authenticationManager;

	private final AuthAccessRepository authAccessRepository;

	private final JwtUserDetailsService userDetailsService;

	private final ManagementService managementService;

	private final JwtTokenService jwtTokenUtil;

	public AuthenticationService(AuthenticationManager authenticationManager, AuthAccessRepository authAccessRepository,
			ManagementService managementService, JwtUserDetailsService userDetailsService,
			JwtTokenService jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.authAccessRepository = authAccessRepository;
		this.userDetailsService = userDetailsService;
		this.managementService = managementService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.bcrypt = new BCryptPasswordEncoder();
	}
	
	@Override
	protected void validate(AuthAccess entity) {
		if (!entity.hasValidOrganization())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Organization values");

		if (!StringUtils.hasText(entity.getEmail()) || !StringUtils.hasText(entity.getPassword()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid AuthAccess values");
	}

	private void authenticate(JwtRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (DisabledException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
		}
	}

	public Map<String, Object> authenticateAccess(JwtRequest authRequest) {
		authenticate(authRequest);

		final var authAccess = authAccessRepository.findByEmail(authRequest.getEmail());
		final var jwt = jwtTokenUtil.generateToken(authAccess.getId(), authAccess.getOrganization().getAlias(),
				authAccess.getDefaultClaims());

		var response = new HashMap<String, Object>();
		response.put(JwtKeys.ACCESS.toString(), authAccess);
		response.put(JwtKeys.TOKEN.toString(), jwt[0]);
		response.put(JwtKeys.REFRESH_TOKEN.toString(), jwt[1]);

		authAccess.setTokenHash(bcrypt.encode(jwt[0]));
		authAccessRepository.save(authAccess);

		return response;
	}
	
	public Map<String, Object> authenticateRefreshAccess(HttpServletRequest request,
			JwtRefreshRequest authRefreshRequest) throws ServiceException {
		
		// Validates Bearer token was sent
		var tokenOpt = JwtRequestFilter.getJwtFromRequest(request);
		if (!tokenOpt.isPresent())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		// Read token/playload
		var token = tokenOpt.get();
		var payload = jwtTokenUtil.readClaims(token);

		// Validates refresh token
		if (!jwtTokenUtil.isRefreshTokenValid(token, authRefreshRequest.getRefreshToken()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		// Build Claims and new JWT
		var claims = new HashMap<String, Object>();
		claims.put(JwtKeys.ROLES.toString(), payload.getRoles());
		final var jwt = jwtTokenUtil.generateToken(payload.getSub(), payload.getAud(), claims);

		// Prepare response
		var response = new HashMap<String, Object>();
		response.put(JwtKeys.TOKEN.toString(), jwt[0]);
		response.put(JwtKeys.REFRESH_TOKEN.toString(), jwt[1]);
		
		// Update root user
		if (payload.getRoles().contains(ServicesRole.ROOT.name())) {
			final var authAccessOpt = authAccessRepository.findById(payload.getSub());
			final var authAccess = authAccessOpt.get();
			authAccess.setTokenHash(bcrypt.encode(jwt[0]));
			authAccessRepository.save(authAccess);
		}

		return response;
	}

	public AuthAccess getAuthenticated(HttpServletRequest request) throws ServiceException {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		var token = JwtRequestFilter.getJwtFromRequest(request);
		if (!token.isPresent())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		var roles = jwtTokenUtil.getClaimFromToken(token.get(),
				claims -> claims.get(JwtKeys.ROLES.toString(), List.class));

		if (roles.contains(ServicesRole.ROOT.name())) {
			return getRootAuthenticated(request, authentication, token.get());
		} else if (roles.contains(ServicesRole.AGENT.name()) || 
				roles.contains(ServicesRole.SESSION.name()) ||
				roles.contains(ServicesRole.INTERNAL.name())) {
			return getInternalAuthenticated(request, token.get(), roles);
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	public AuthAccess getRootAuthenticated(HttpServletRequest request, Authentication authentication, String token) {
		var authAccess = authAccessRepository.findById(authentication.getName()).get();
		if (authAccess == null || !bcrypt.matches(token, authAccess.getTokenHash()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);
		if (!StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		return authAccess;
	}

	public AuthAccess getInternalAuthenticated(HttpServletRequest request, String token, List<?> roles) {
		var online = false;
		if (roles.contains(ServicesRole.AGENT.name()))
			online = managementService.isOnline(request);
		else if (roles.contains(ServicesRole.INTERNAL.name()))
			online = true;

		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);

		if (!online || !StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		return new AuthAccess(jwtTokenUtil.getUsernameFromToken(token), orgAlias);
	}

	public AuthAccess registerAccess(AccessRequest accessRequest) {
		var authAccess = accessRequest.getAuthAccess();
		this.validate(authAccess);

		return userDetailsService.newUser(authAccess);
	}

	public void logoff(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		final AuthAccess authAccess = (AuthAccess) getAuthenticated(request);

		if (authAccess.isRoot()) {
			authAccess.setTokenHash(null);
			authAccessRepository.save(authAccess);
		} else {
			managementService.logoff(request);
		}

		new SecurityContextLogoutHandler().logout(request, response,
				SecurityContextHolder.getContext().getAuthentication());
	}

}
