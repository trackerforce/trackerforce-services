package com.trackerforce.session.service;

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
import com.trackerforce.session.model.SessionProcedure;

@Service
public class QueueService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${service.queue.url}/queue")
	private String serviceUrl;

	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.TENANT_HEADER.toString(), request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}

	public void submitProcedure(HttpServletRequest request, SessionProcedure procedure, String contextId) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var tenantId = request.getHeader(RequestHeader.TENANT_HEADER.toString());
			var url = String.format("%s%s%s/%s", serviceUrl, "/session/v1/procedure/submit/", tenantId, contextId);
			restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(procedure, headers), Object.class);
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

}
