package com.trackerforce.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.request.AgentRequest;

@CrossOrigin(allowedHeaders = { "X-Tenant" })
@RestController
@RequestMapping("management/agent")
public class AgentController {
	
	@PostMapping(value = "/v1/create")
	public ResponseEntity<?> create(@RequestBody AgentRequest agentRequest) {
		//TODO
		agentRequest.setPassword("temporary");
		return ResponseEntity.ok(agentRequest);
	}
	
}
