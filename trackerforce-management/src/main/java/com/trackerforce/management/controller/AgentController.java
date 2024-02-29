package com.trackerforce.management.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.service.AgentService;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("management/agent/v1")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@PostMapping(value = "/create")
	public ResponseEntity<?> create(HttpServletRequest request, @RequestBody AgentRequest agentRequest) {
		try {
			return ResponseEntity.ok(agentService.create(request, agentRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@PostMapping(value = "/activate")
	public ResponseEntity<?> activate(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.activate(agentRequest));
	}

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.login(agentRequest, null));
	}

	@PostMapping(value = "/logoff")
	public ResponseEntity<?> logoff(HttpServletRequest request) {
		agentService.logoff(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/watch/{agentId}/{caseId}")
	public ResponseEntity<?> watch(
			@PathVariable(value = "agentId") String agentId,
			@PathVariable(value = "caseId") String caseId) {
		return ResponseEntity.ok(agentService.watchCase(agentId, caseId));
	}
	
	@PostMapping(value = "/unwatch/{agentId}/{caseId}")
	public ResponseEntity<?> unwatch(
			@PathVariable(value = "agentId") String agentId,
			@PathVariable(value = "caseId") String caseId) {
		return ResponseEntity.ok(agentService.unWatchCase(agentId, caseId));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findOne(@PathVariable(value="id") String id, String output) {
		return ResponseEntity.ok(agentService.findByIdProjectedBy(id, output));
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<Map<String, Object>> findAll(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		var query = new HashMap<String, Object>();
		query.put("name", name);
		query.put("email", email);
		
		var queryable = new QueryableRequest(query, sortBy, output, page, size);
		return ResponseEntity.ok(agentService.findAllProjectedBy(queryable));
	}

	@GetMapping(value = "/me")
	public ResponseEntity<?> getAuthenticated(HttpServletRequest request) {
		return ResponseEntity.ok(agentService.getAuthenticated(request));
	}

	@GetMapping(value = "/check")
	public ResponseEntity<?> check(HttpServletRequest request) {
		return ResponseEntity.ok(agentService.isOnline(request));
	}

}
