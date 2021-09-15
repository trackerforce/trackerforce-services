package com.trackerforce.session.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.trackerforce.session.model.SessionProcedure;

@Service
public class MLEngineService {

	private RestTemplate restTemplate = new RestTemplate();

	public Object predictProcedure(String serviceUrl, SessionProcedure procedureRequest) {
		restTemplate.exchange(String.format("%s%s", serviceUrl, "/predict/v1/"), HttpMethod.GET,
				new HttpEntity<>(procedureRequest, new HttpHeaders()), Object.class);
		
		// TODO: Implement prdiction result
		return null;
	}

}
