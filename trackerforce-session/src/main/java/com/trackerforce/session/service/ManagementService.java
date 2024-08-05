package com.trackerforce.session.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.model.type.RequestHeader;
import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.SessionTemplate;
import com.trackerforce.session.model.response.GlobalResponse;

@Service
public class ManagementService {

	private static final String PROCEDURE_V1 = "procedure/v1/";
	private static final String TEMPLATE_V1 = "template/v1/";
	private static final String AGENT_V1 = "agent/v1/";
	private static final String GLOBAL_V1 = "global/v1/";

	private final RestTemplate restTemplate = new RestTemplate();

	private final String serviceUrl;

	public ManagementService(@Value("${service.management.url}/management/") String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	private void setHeaders(HttpServletRequest request, HttpHeaders headers) {
		headers.add(RequestHeader.AUTHORIZATION.toString(), request.getHeader(RequestHeader.AUTHORIZATION.toString()));
		headers.add(RequestHeader.TENANT_HEADER.toString(), request.getHeader(RequestHeader.TENANT_HEADER.toString()));
	}
	
	public SessionTemplate findTemplate(HttpServletRequest request, String id) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var response = restTemplate.exchange(serviceUrl + TEMPLATE_V1 + id, HttpMethod.GET,
					new HttpEntity<>(null, headers), SessionTemplate.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
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
			var url = "";
			if (StringUtils.isBlank(output)) {
				url = String.format("%s%s%s", serviceUrl, PROCEDURE_V1, id);
			} else {
				url = String.format("%s%s%s?output=%s", serviceUrl, PROCEDURE_V1, id, output);
			}
				
			var response = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>(null, headers), SessionProcedure.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findAll(HttpServletRequest request, QueryableRequest queryableRequest) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var name = queryableRequest.getQuery().get("name");
			var url = String.format("%s%s?output=id,name,description&page=%s&name=%s", serviceUrl, PROCEDURE_V1,
					queryableRequest.getPage(), name);

			var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), Map.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}
	
	public GlobalResponse findMLServiceUrl(HttpServletRequest request) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var url = String.format("%s%s", serviceUrl, GLOBAL_V1 + "ML_SERVICE");
			var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers),
					GlobalResponse.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}

	public void watchCase(HttpServletRequest request, String id, String agentId) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var url = String.format("%s%s%s/%s", serviceUrl, AGENT_V1 + "watch/", agentId, id);
			restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(null, headers), Object.class);
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}
	
	public AgentResponse findAgentCases(HttpServletRequest request, String agentId) {
		var headers = new HttpHeaders();
		setHeaders(request, headers);

		try {
			var url = String.format("%s%s/%s", serviceUrl, AGENT_V1, agentId);
			var response = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>(null, headers), AgentResponse.class);

			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
		}
	}

}
