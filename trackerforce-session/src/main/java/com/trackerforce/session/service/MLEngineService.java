package com.trackerforce.session.service;

import static com.github.switcherapi.client.SwitcherContext.getSwitcher;
import static com.trackerforce.session.config.Features.ML_SERVICE;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.response.PredictionResponse;

@Service
public class MLEngineService {

	private final RestTemplate restTemplate = new RestTemplate();

	public PredictionResponse predictProcedure(String serviceUrl, String tenantId,
			SessionProcedure procedure, String contextId) {
		procedure.setContextId(contextId);
		if (getSwitcher(ML_SERVICE).isItOn()) {
			var prediction = restTemplate.exchange(String.format("%s%s%s", serviceUrl, "/predict/v1/", tenantId),
					HttpMethod.POST, new HttpEntity<>(procedure, new HttpHeaders()), PredictionResponse.class);
			
			return prediction.getBody();
		}
		
		return mockResponse();
	}
	
	private PredictionResponse mockResponse() {
		var response = new PredictionResponse();
		response.setAccuracy(0);
		return response;
	}

}
