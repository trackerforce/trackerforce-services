package com.trackerforce.session.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.service.SessionCaseService;

@CrossOrigin(methods = { RequestMethod.POST })
@RestController
@RequestMapping("session")
public class SessionCaseController {

	private final SessionCaseService sessionCaseService;

	public SessionCaseController(SessionCaseService sessionCaseService) {
		this.sessionCaseService = sessionCaseService;
	}

	@PostMapping(value = "/v1")
	public ResponseEntity<?> create(HttpServletRequest request, @RequestBody SessionCaseRequest sessionCaseRequest) {
		try {
			return ResponseEntity.ok(sessionCaseService.hanlder(request, sessionCaseRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@PostMapping(value = "/v1/procedure/submit")
	public ResponseEntity<?> submitProcedure() {
		// TODO
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/v1/procedure/select")
	public ResponseEntity<?> selectProcedure() {
		// TODO
		return ResponseEntity.ok().build();
	}

}
