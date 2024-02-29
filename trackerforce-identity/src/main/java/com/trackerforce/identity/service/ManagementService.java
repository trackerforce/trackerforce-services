package com.trackerforce.identity.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.model.type.RequestHeader;

@Service
public class ManagementService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${service.management.url}/management/")
	private String serviceUrl;

	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.AUTHORIZATION.toString(), request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}

	public AgentResponse activateAgent(HttpServletRequest request, AgentRequest agentRequest) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + "agent/v1/activate", HttpMethod.POST,
					new HttpEntity<>(agentRequest, headers), AgentResponse.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

	public AgentResponse login(HttpServletRequest request, AgentRequest agentRequest) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + "agent/v1/login", HttpMethod.POST,
					new HttpEntity<>(agentRequest, headers), AgentResponse.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

	public void logoff(HttpServletRequest request) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			restTemplate.exchange(serviceUrl + "agent/v1/logoff", HttpMethod.POST, new HttpEntity<>(headers),
					Void.class);
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

	public Boolean isOnline(HttpServletRequest request) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + "agent/v1/check", HttpMethod.GET,
					new HttpEntity<>(headers), Boolean.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

}
