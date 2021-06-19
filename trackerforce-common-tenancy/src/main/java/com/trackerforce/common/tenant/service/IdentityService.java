package com.trackerforce.common.tenant.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.trackerforce.common.config.JwtRequestFilter.AUTHORIZATION;
import static com.trackerforce.common.tenant.interceptor.TenantInterceptor.TENANT_HEADER;;

@Service
public class IdentityService {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${service.identity.url}/identity/v1/valid")
	private String serviceUrl;
	
	public boolean validateIdentity(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, request.getHeader(AUTHORIZATION));
		headers.add(TENANT_HEADER, request.getHeader(TENANT_HEADER));
		
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

}
