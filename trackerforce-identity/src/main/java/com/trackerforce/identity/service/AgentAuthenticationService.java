package com.trackerforce.identity.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.config.RequestHeader;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;

import io.jsonwebtoken.Claims;

@Service
public class AgentAuthenticationService {
	
	public static final String ACCESS = "access";
	
	public static final String TOKEN = "token";
	
	public static final String REFRESH_TOKEN = "refreshToken";
	
	private final JwtTokenService jwtTokenUtil;
	
	private final ManagementService managementService;
	
	public AgentAuthenticationService(
			JwtTokenService jwtTokenUtil,
			ManagementService managementService) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.managementService = managementService;
	}
	
	public HashMap<String, Object> activateAgent(HttpServletRequest request, 
			AgentRequest accessRequest) throws ServiceException {
		var agentResponse = managementService.activateAgent(request, accessRequest);
		authenticate(request, agentResponse);
		
		var claims = new HashMap<String, Object>();
		claims.put(JwtTokenService.ROLES, agentResponse.getRoles());
		
		final var jwt = jwtTokenUtil.generateToken(agentResponse.getEmail(), 
				request.getHeader(RequestHeader.TENANT_HEADER.toString()), 
				claims);
		
		var response = new HashMap<String, Object>();
		agentResponse.setTempAccess(null);
		response.put(ACCESS, agentResponse);
		response.put(TOKEN, jwt[0]);
		response.put(REFRESH_TOKEN, jwt[1]);
		
		return response;
	}
	
	public AgentResponse getAuthenticated(HttpServletRequest request, 
			Authentication authentication, String token) throws ServiceException {
		var agentRequest = new AgentRequest();
		agentRequest.setEmail(authentication.getName());
		
		var authAccess = managementService.findAgent(request, agentRequest);
		if (authAccess == null || !authentication.isAuthenticated())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token, Claims::getAudience);
		if (!StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		return authAccess;
	}
	
	private void authenticate(HttpServletRequest request, AgentResponse agentResponse) {
		final UsernamePasswordAuthenticationToken authUser = 
				new UsernamePasswordAuthenticationToken(
						agentResponse.getEmail(), agentResponse.getTempAccess(), new ArrayList<>());
		
		authUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authUser);
	}

}
