package com.trackerforce.session.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.type.RequestHeader;
import com.trackerforce.session.model.SessionProcedure;

@Service
public class QueueService {

	private final RestTemplate restTemplate = new RestTemplate();

	private final String serviceUrl;

	public QueueService(@Value("${service.queue.url}/queue/session") String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.TENANT_HEADER.toString(), request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}

	public void submitProcedure(HttpServletRequest request, SessionProcedure procedure, String contextId) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var tenantId = request.getHeader(RequestHeader.TENANT_HEADER.toString());
			var url = String.format("%s%s%s/%s", serviceUrl, "/v1/procedure/submit/", tenantId, contextId);
			restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(procedure, headers), Object.class);
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}
	
	public void nextProcedure(HttpServletRequest request, SessionProcedure procedure, String contextId) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var tenantId = request.getHeader(RequestHeader.TENANT_HEADER.toString());
			var url = String.format("%s%s%s/%s", serviceUrl, "/v1/procedure/next/", tenantId, contextId);
			restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(procedure, headers), Object.class);
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}

}
