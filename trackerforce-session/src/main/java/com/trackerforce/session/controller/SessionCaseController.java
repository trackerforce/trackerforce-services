package com.trackerforce.session.controller;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.model.request.SessionProcedureRequest;
import com.trackerforce.session.service.SessionCaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("session/case/v1")
public class SessionCaseController {

	private final SessionCaseService sessionCaseService;

	public SessionCaseController(SessionCaseService sessionCaseService) {
		this.sessionCaseService = sessionCaseService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<SessionCase> create(HttpServletRequest request,
									@RequestBody SessionCaseRequest sessionCaseRequest) {
		return ResponseEntity.ok(sessionCaseService.create(request, sessionCaseRequest));
	}

	@PostMapping(value = "/handler")
	public ResponseEntity<SessionProcedure> handler(HttpServletRequest request,
													@RequestBody SessionProcedureRequest sessionProcedureRequest) {
		return ResponseEntity.ok(sessionCaseService.handlerProcedure(request, sessionProcedureRequest));
	}

	@PostMapping(value = "/ids")
	public ResponseEntity<Map<String, Object>> findByIds(HttpServletRequest request,
			@RequestBody SessionCaseRequest sessionCaseRequest,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(sessionCaseService.findByIdsProjectedBy(
				sessionCaseRequest.getCaseIds(), sortBy, output, page, size));
	}
	
	@PostMapping(value = "/next")
	public ResponseEntity<Map<String, Object>> next(HttpServletRequest request,
			@RequestBody SessionProcedureRequest sessionProcedureRequest,
			@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		var query = new HashMap<String, Object>();
		query.put("name", name != null ? name : StringUtils.EMPTY);

		var queryable = new QueryableRequest(query, page, 10);
		return ResponseEntity.ok(sessionCaseService.next(request, sessionProcedureRequest, queryable));
	}

	@GetMapping(value = "/protocol/{protocol}")
	public ResponseEntity<SessionCase> findByProtocol(
			@PathVariable("protocol") String protocol) {
		return ResponseEntity.ok(sessionCaseService.getSessionCaseByProtocol(protocol));
	}
	
	@GetMapping(value = "/agent/{agentid}")
	public ResponseEntity<Map<String, Object>> findByAgent(HttpServletRequest request,
			@PathVariable("agentid") String agentid,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(sessionCaseService.getSessionCaseByAgent(request, agentid, sortBy,
				output, page, size));
	}

}
