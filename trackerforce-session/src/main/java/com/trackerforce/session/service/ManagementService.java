package com.trackerforce.session.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.model.type.RequestHeader;
import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.SessionTemplate;
import com.trackerforce.session.model.response.GlobalResponse;

@Service
public class ManagementService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${service.management.url}/management/")
	private String serviceUrl;

	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.AUTHORIZATION.toString(), request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}
	
	public SessionTemplate findTemplate(HttpServletRequest request, String id) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + "template/v1/" + id, HttpMethod.GET,
					new HttpEntity<>(null, headers), SessionTemplate.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}
	
	public SessionProcedure findProcedureShort(HttpServletRequest request, String id) {
		return findProcedure(request, id, "id,name,description");
	}
	
	public SessionProcedure findProcedure(HttpServletRequest request, String id) {
		return findProcedure(request, id, null);
	}

	private SessionProcedure findProcedure(HttpServletRequest request, String id, String output) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var url = String.format("%s%s%s?output=%s", serviceUrl, "procedure/v1/", id, output);
			var response = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>(null, headers), SessionProcedure.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findAll(HttpServletRequest request, QueryableRequest queryableRequest) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var name = queryableRequest.getQuery().get("name");
			var url = String.format("%s%s?output=id,name,description&page=%s&name=%s", serviceUrl, "procedure/v1/",
					queryableRequest.getPage(), name);

			var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), Map.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getRawStatusCode(), e.getMessage(), e);
		}
	}
	
	public GlobalResponse findMLServiceUrl(HttpServletRequest request) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var url = String.format("%s%s", serviceUrl, "global/v1/ML_SERVICE");
			var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers),
					GlobalResponse.class);

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
