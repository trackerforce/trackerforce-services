package com.trackerforce.session.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.model.request.SessionProcedureRequest;
import com.trackerforce.session.service.SessionCaseService;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("session/case/v1")
public class SessionCaseController {

	private final SessionCaseService sessionCaseService;

	public SessionCaseController(SessionCaseService sessionCaseService) {
		this.sessionCaseService = sessionCaseService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<?> create(HttpServletRequest request,
									@RequestBody SessionCaseRequest sessionCaseRequest) {
		try {
			return ResponseEntity
					.ok(sessionCaseService.create(request, sessionCaseRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@PostMapping(value = "/handler")
	public ResponseEntity<?> handler(HttpServletRequest request,
			@RequestBody SessionProcedureRequest sessionProcedureRequest) {
		try {
			return ResponseEntity.ok(sessionCaseService.handlerProcedure(request,
					sessionProcedureRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@PostMapping(value = "/ids")
	public ResponseEntity<?> findByIds(HttpServletRequest request,
			@RequestBody SessionCaseRequest sessionCaseRequest,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(sessionCaseService.findByIdsProjectedBy(
				sessionCaseRequest.getCaseIds(), sortBy, output, page, size));
	}
	
	@PostMapping(value = "/next")
	public ResponseEntity<?> next(HttpServletRequest request,
			@RequestBody SessionProcedureRequest sessionProcedureRequest,
			@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			var query = new HashMap<String, Object>();
			query.put("name", name != null ? name : StringUtils.EMPTY);

			var queryable = new QueryableRequest(query, page, 10);
			return ResponseEntity.ok(
					sessionCaseService.next(request, sessionProcedureRequest, queryable));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
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
