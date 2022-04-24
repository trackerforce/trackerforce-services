package com.trackerforce.identity.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.model.type.JwtKeys;
import com.trackerforce.common.model.type.RequestHeader;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.identity.model.dto.response.AuthAgentResponseDTO;

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
	
	/**
	 * Add Agent to context and generate JWT credentials
	 * 
	 * @param request
	 * @param agentResponse
	 * @return
	 */
	private AuthAgentResponseDTO authenticate(HttpServletRequest request, AgentResponse agentResponse) {
		final UsernamePasswordAuthenticationToken authUser = 
				new UsernamePasswordAuthenticationToken(
						agentResponse.getEmail(), agentResponse.getTempAccess(), new ArrayList<>());
		
		authUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authUser);
		
		var claims = new HashMap<String, Object>();
		claims.put(JwtKeys.ROLES.toString(), agentResponse.getRoles());
		
		final var jwt = jwtTokenUtil.generateToken(agentResponse.getId(), 
				request.getHeader(RequestHeader.TENANT_HEADER.toString()), 
				claims);
		
		agentResponse.setTempAccess(null);
		return new AuthAgentResponseDTO(agentResponse, jwt[0], jwt[1]);
	}
	
	public AuthAgentResponseDTO activateAgent(HttpServletRequest request, 
			AgentRequest agentRequest) {
		var agentResponse = managementService.activateAgent(request, agentRequest);
		return authenticate(request, agentResponse);
	}
	
	public AuthAgentResponseDTO authenticateAccess(HttpServletRequest request, 
			AgentRequest agentRequest) {
		var agentResponse = managementService.login(request, agentRequest);
		return authenticate(request, agentResponse);
	}

}
