package com.trackerforce.management.controller;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("management/agent/v1")
public class AgentController {

	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		this.agentService = agentService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<AgentResponse> create(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.create(agentRequest));
	}

	@PostMapping(value = "/activate")
	public ResponseEntity<AgentResponse> activate(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.activate(agentRequest));
	}

	@PostMapping(value = "/login")
	public ResponseEntity<AgentResponse> login(@RequestBody AgentRequest agentRequest) {
		return ResponseEntity.ok(agentService.login(agentRequest, null));
	}

	@PostMapping(value = "/logoff")
	public ResponseEntity<Object> logoff() {
		agentService.logoff();
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/watch/{agentId}/{caseId}")
	public ResponseEntity<AgentResponse> watch(
			@PathVariable(value = "agentId") String agentId,
			@PathVariable(value = "caseId") String caseId) {
		return ResponseEntity.ok(agentService.watchCase(agentId, caseId));
	}
	
	@PostMapping(value = "/unwatch/{agentId}/{caseId}")
	public ResponseEntity<AgentResponse> unwatch(
			@PathVariable(value = "agentId") String agentId,
			@PathVariable(value = "caseId") String caseId) {
		return ResponseEntity.ok(agentService.unWatchCase(agentId, caseId));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Agent> findOne(@PathVariable(value="id") String id, String output) {
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
	public ResponseEntity<Agent> getAuthenticated() {
		return ResponseEntity.ok(agentService.getAuthenticated());
	}

	@GetMapping(value = "/check")
	public ResponseEntity<Boolean> check() {
		return ResponseEntity.ok(agentService.isOnline());
	}

}
