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
import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTemplate;

@Service
public class ManagementService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${service.management.url}/management/")
	private String serviceUrl;

	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.AUTHORIZATION.toString(), request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}

	public CommonTemplate findTemplate(HttpServletRequest request, String id) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + "template/v1/" + id, HttpMethod.GET,
					new HttpEntity<>(null, headers), CommonTemplate.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

	public CommonProcedure findProcedure(HttpServletRequest request, String id) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + "procedure/v1/" + id, HttpMethod.GET,
					new HttpEntity<>(null, headers), CommonProcedure.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

	public void watchCase(HttpServletRequest request, String id, String agentId) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var url = String.format("%s%s%s/%s", serviceUrl, "agent/v1/watch/", agentId, id);
			restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(null, headers), Object.class);
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}

}
