package com.trackerforce.session.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.model.request.SessionProcedureRequest;
import com.trackerforce.session.service.SessionCaseService;

@CrossOrigin(methods = { RequestMethod.POST })
@RestController
@RequestMapping("session/case")
public class SessionCaseController {

	private final SessionCaseService sessionCaseService;

	public SessionCaseController(SessionCaseService sessionCaseService) {
		this.sessionCaseService = sessionCaseService;
	}

	@PostMapping(value = "/v1")
	public ResponseEntity<?> create(HttpServletRequest request, @RequestBody SessionCaseRequest sessionCaseRequest) {
		try {
			return ResponseEntity.ok(sessionCaseService.create(request, sessionCaseRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@PostMapping(value = "/v1/handler")
	public ResponseEntity<?> handler(HttpServletRequest request,
			@RequestBody SessionProcedureRequest sessionProcedureRequest) {
		try {
			return ResponseEntity.ok(sessionCaseService.handlerProcedure(request, sessionProcedureRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@GetMapping(value = "/v1/ids")
	public ResponseEntity<?> findByIds(HttpServletRequest request, 
			@RequestBody SessionCaseRequest sessionCaseRequest,
			@RequestParam(required = false) String sortBy, 
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(sessionCaseService.findByIdsProjectedBy(sessionCaseRequest.getCaseIds(), sortBy,
				output, page, size));
	}

}
