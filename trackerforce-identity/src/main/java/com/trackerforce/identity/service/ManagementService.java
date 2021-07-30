package com.trackerforce.identity.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.config.RequestHeader;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.exception.ServiceException;

@Service
public class ManagementService {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${service.management.url}/management/")
	private String serviceUrl;
	
	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.AUTHORIZATION.toString(), 
				request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), 
				request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}
	
	public AgentResponse activateAgent(HttpServletRequest request, 
			AgentRequest agentRequest) throws ServiceException {
		
		var headers = new HttpHeaders();
		setHeaders(request, headers);
		
		try {
			var response = restTemplate.exchange(
					serviceUrl + "agent/v1/activate", 
					HttpMethod.POST, 
					new HttpEntity<>(agentRequest, headers), 
					AgentResponse.class);
			
			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}
	
	public AgentResponse findAgent(HttpServletRequest request, 
			AgentRequest agentRequest) throws ServiceException {
		
		var headers = new HttpHeaders();
		setHeaders(request, headers);
		
		try {
			var response = restTemplate.exchange(
					serviceUrl + "agent/v1/find", 
					HttpMethod.GET, 
					new HttpEntity<>(agentRequest, headers), 
					AgentResponse.class);
			
			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

}
