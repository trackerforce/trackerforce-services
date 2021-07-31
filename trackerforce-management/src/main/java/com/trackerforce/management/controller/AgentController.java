package com.trackerforce.management.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.service.AgentService;

@CrossOrigin(allowedHeaders = { "X-Tenant" })
@RestController
@RequestMapping("management/agent")
public class AgentController {
	
	@Autowired
	private AgentService agentService;
	
	@PostMapping(value = "/v1/create")
	public ResponseEntity<?> create(@RequestBody AgentRequest agentRequest) {
		try {
			return ResponseEntity.ok(agentService.create(agentRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PostMapping(value = "/v1/activate")
	public ResponseEntity<?> activate(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.activate(agentRequest));
	}
	
	@PostMapping(value = "/v1/login")
	public ResponseEntity<?> login(HttpServletRequest request) {
		System.out.println("Logged");
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value = "/v1/find")
	public ResponseEntity<?> find(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.findAgent(agentRequest));
	}
	
	@GetMapping(value = "/v1/me")
	public ResponseEntity<?> getAuthenticated(HttpServletRequest request) {
		try {
			return ResponseEntity.ok(agentService.getAuthenticated(request));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
}
