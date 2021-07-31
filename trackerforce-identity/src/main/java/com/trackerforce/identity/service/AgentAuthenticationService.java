package com.trackerforce.identity.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.trackerforce.common.config.RequestHeader;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.model.type.JwtKeys;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;

@Service
public class AgentAuthenticationService {
	
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
		claims.put(JwtKeys.ROLES.toString(), agentResponse.getRoles());
		
		final var jwt = jwtTokenUtil.generateToken(agentResponse.getEmail(), 
				request.getHeader(RequestHeader.TENANT_HEADER.toString()), 
				claims);
		
		var response = new HashMap<String, Object>();
		agentResponse.setTempAccess(null);
		response.put(JwtKeys.ACCESS.toString(), agentResponse);
		response.put(JwtKeys.TOKEN.toString(), jwt[0]);
		response.put(JwtKeys.REFRESH_TOKEN.toString(), jwt[1]);
		
		return response;
	}
	
	private void authenticate(HttpServletRequest request, AgentResponse agentResponse) {
		final UsernamePasswordAuthenticationToken authUser = 
				new UsernamePasswordAuthenticationToken(
						agentResponse.getEmail(), agentResponse.getTempAccess(), new ArrayList<>());
		
		authUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authUser);
	}

}
