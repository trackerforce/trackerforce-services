package com.trackerforce.common.tenant.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.trackerforce.common.model.type.RequestHeader;;

@Service
public class IdentityService {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${service.identity.url}/identity/v1/valid")
	private String serviceUrl;
	
	@Value("${service.endpoint.allowed-endpoints}")
	protected String[] allowedEndpoints;
	
	public boolean validateIdentity(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(RequestHeader.AUTHORIZATION.toString(), 
				request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), 
				request.getHeader(RequestHeader.TENANT_HEADER.toString()));
		
		try {
			var response = restTemplate.exchange(serviceUrl, 
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					Boolean.class);
			
			return response.getBody().booleanValue();			
		} catch (RestClientException e) {
			return false;
		}
	}

	public String[] getAllowedEndpoints() {
		return allowedEndpoints;
	}

}
