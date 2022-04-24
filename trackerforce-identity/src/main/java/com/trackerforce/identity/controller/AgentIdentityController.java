package com.trackerforce.identity.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.identity.model.dto.response.AuthAgentResponseDTO;
import com.trackerforce.identity.service.AgentAuthenticationService;

@CrossOrigin
@RestController
@RequestMapping("identity/agent/v1")
public class AgentIdentityController {
	
	private final AgentAuthenticationService agentAuthorizationService;
	
	public AgentIdentityController(AgentAuthenticationService agentAuthorizationService) {
		this.agentAuthorizationService = agentAuthorizationService;
	}

	@PostMapping(value = "/activate")
	public ResponseEntity<AuthAgentResponseDTO> activateAgent(HttpServletRequest request,
			@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentAuthorizationService.activateAgent(request, agentRequest));	
	}
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<AuthAgentResponseDTO> authenticateAccess(HttpServletRequest request,
			@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentAuthorizationService.authenticateAccess(request, agentRequest));
	}

}
