package com.trackerforce.identity.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.service.exception.ServiceException;

@Service
public class AgentAuthenticationService {
	
	private final ManagementService managementService;
	
	public AgentAuthenticationService(ManagementService managementService) {
		this.managementService = managementService;
	}
	
	public Map<String, Object> registerAccess(HttpServletRequest request, 
			AgentRequest accessRequest) throws ServiceException {
		
		var agent = managementService.registerAgent(request, accessRequest);
		var response = new HashMap<String, Object>();
		response.put("name", agent.getName());
		response.put("email", agent.getEmail());
		response.put("password", agent.getPassword());
		
		return response;
	}

}
