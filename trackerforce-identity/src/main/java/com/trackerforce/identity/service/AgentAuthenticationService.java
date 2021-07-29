package com.trackerforce.identity.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.exception.ServiceException;

@Service
public class AgentAuthenticationService {
	
	private final ManagementService managementService;
	
	public AgentAuthenticationService(ManagementService managementService) {
		this.managementService = managementService;
	}
	
	public AgentResponse registerAccess(HttpServletRequest request, 
			AgentRequest accessRequest) throws ServiceException {
		
		var agent = managementService.registerAgent(request, accessRequest);
		var response = new AgentResponse();
		response.setName(agent.getName());
		response.setEmail(agent.getEmail());
		response.setTemp_access(agent.getPassword());
		
		return response;
	}
	
	public AgentResponse activateAgent(HttpServletRequest request, 
			AgentRequest accessRequest) throws ServiceException {
		var agent = managementService.activateAgent(request, accessRequest);
		var response = new AgentResponse();
		response.setName(agent.getName());
		response.setEmail(agent.getEmail());
		
		return response;
	}

}
