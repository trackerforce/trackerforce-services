package com.trackerforce.identity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.dto.request.AccessRequestDTO;
import com.trackerforce.identity.model.dto.request.JwtRefreshRequestDTO;
import com.trackerforce.identity.model.dto.request.JwtRequestDTO;
import com.trackerforce.identity.model.dto.response.AuthRootResponseDTO;
import com.trackerforce.identity.model.dto.response.AuthAgentResponseDTO;
import com.trackerforce.identity.model.dto.response.AuthResponseDTO;
import com.trackerforce.identity.model.mapper.AuthAccessMapper;
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

	private void authenticate(JwtRequestDTO authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (DisabledException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
		}
	}
	
	private AuthRootResponseDTO getRootAuthenticated(HttpServletRequest request, Authentication authentication, String token) {
		var authAccessOpt = authAccessRepository.findById(authentication.getName());
		if (!authAccessOpt.isPresent() || !bcrypt.matches(token, authAccessOpt.get().getTokenHash()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);
		if (!StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		return new AuthRootResponseDTO(authAccessOpt.get(), token, null);
	}

	private AuthAgentResponseDTO getInternalAuthenticated(HttpServletRequest request, String token, List<?> roles) {
		var online = false;
		if (roles.contains(ServicesRole.AGENT.name()))
			online = managementService.isOnline(request);
		else if (roles.contains(ServicesRole.INTERNAL.name()))
			online = true;

		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);

		if (!online || !StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		final var roleList = roles.stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList());

		return new AuthAgentResponseDTO(new AuthAccess(jwtTokenUtil.getUsernameFromToken(token), orgAlias), roleList,
				online);
	}

	public AuthRootResponseDTO authenticateRootAccess(JwtRequestDTO authRequest) {
		authenticate(authRequest);

		final var authAccess = authAccessRepository.findByEmail(authRequest.getEmail());
		final var jwt = jwtTokenUtil.generateToken(authAccess.getId(), authAccess.getOrganization().getAlias(),
				authAccess.getDefaultClaims());

		authAccess.setTokenHash(bcrypt.encode(jwt[0]));
		authAccessRepository.save(authAccess);
		return new AuthRootResponseDTO(authAccess, jwt[0], jwt[1]);
	}
	
	public AuthResponseDTO authenticateRefreshAccess(HttpServletRequest request,
			JwtRefreshRequestDTO authRefreshRequest) {
		
		// Validates Bearer token was sent
		var tokenOpt = JwtRequestFilter.getJwtFromRequest(request);
		if (!tokenOpt.isPresent())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		// Read from token
		final var token = tokenOpt.get();
		final var payload = jwtTokenUtil.readClaims(token);

		// Validates refresh token
		if (!jwtTokenUtil.isRefreshTokenValid(token, authRefreshRequest.getRefreshToken()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		// Build Claims and new JWT
		final var claims = new HashMap<String, Object>();
		claims.put(JwtKeys.ROLES.toString(), payload.getRoles());
		final var jwt = jwtTokenUtil.generateToken(payload.getSub(), payload.getAud(), claims);
		
		// Update root user
		if (payload.getRoles().contains(ServicesRole.ROOT.name())) {
			var authAccessOpt = authAccessRepository.findById(payload.getSub());
			if (!authAccessOpt.isPresent())
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			
			final var authAccess = authAccessOpt.get();
			authAccess.setTokenHash(bcrypt.encode(jwt[0]));
			authAccessRepository.save(authAccess);
		}
		
		return new AuthResponseDTO(jwt[0], jwt[1], null);
	}

	public AuthResponseDTO getAuthenticated(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		var token = JwtRequestFilter.getJwtFromRequest(request);
		if (!token.isPresent())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		var roles = jwtTokenUtil.getClaimFromToken(token.get(),
				claims -> claims.get(JwtKeys.ROLES.toString(), List.class));

		if (roles.contains(ServicesRole.ROOT.name())) {
			return getRootAuthenticated(request, authentication, token.get());
		} else if (roles.contains(ServicesRole.AGENT.name()) 
				|| roles.contains(ServicesRole.SESSION.name())
				|| roles.contains(ServicesRole.INTERNAL.name())) {
			return getInternalAuthenticated(request, token.get(), roles);
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	public AuthRootResponseDTO registerAccess(AccessRequestDTO accessRequest) {
		var authAccess = AuthAccessMapper.getAuthAccess(accessRequest);
		this.validate(authAccess);

		final var newRootAccess = userDetailsService.newUser(authAccess);
		return new AuthRootResponseDTO(newRootAccess, null, null);
	}

	public void logoff(HttpServletRequest request, HttpServletResponse response) {
		final AuthResponseDTO authAccess = getAuthenticated(request);

		if (Boolean.TRUE.equals(authAccess.isRoot())) {
			var authRootOpt = authAccessRepository.findById(authAccess.getId());
			
			if (authRootOpt.isPresent()) {
				var authRoot = authRootOpt.get();
				authRoot.setTokenHash(null);
				authAccessRepository.save(authRoot);				
			}
		} else {
			managementService.logoff(request);
		}

		new SecurityContextLogoutHandler().logout(request, response,
				SecurityContextHolder.getContext().getAuthentication());
	}

}
