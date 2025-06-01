package com.trackerforce.common.tenant.service;

import com.trackerforce.common.model.type.RequestHeader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class IdentityService {
	
	private final RestTemplate restTemplate = new RestTemplate();

	private final String serviceUrl;
	
	@Getter
	protected final String[] allowedEndpoints;

	public IdentityService(@Value("${service.identity.url}/identity/v1/valid") String serviceUrl,
			@Value("${service.endpoint.allowed-endpoints}") String[] allowedEndpoints) {
		this.serviceUrl = serviceUrl;
		this.allowedEndpoints = allowedEndpoints;
	}

	public boolean validateIdentity(HttpServletRequest request) {
		var headers = new HttpHeaders();
		headers.add(RequestHeader.AUTHORIZATION.toString(), 
				request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), 
				request.getHeader(RequestHeader.TENANT_HEADER.toString()));
		
		try {
			var response = restTemplate.exchange(serviceUrl, 
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					Boolean.class);

			return Optional.of(response)
					.map(resp -> resp.getStatusCode().is2xxSuccessful() &&
							Optional.ofNullable(resp.getBody()).orElse(false))
					.orElse(false);
		} catch (RestClientException e) {
			return false;
		}
	}

}
