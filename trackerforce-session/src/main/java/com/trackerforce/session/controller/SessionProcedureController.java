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
import com.trackerforce.session.model.request.SessionProcedureRequest;
import com.trackerforce.session.service.SessionProcedureService;

@CrossOrigin(methods = { RequestMethod.POST })
@RestController
@RequestMapping("session/procedure")
public class SessionProcedureController {

	private final SessionProcedureService sessionProcedureService;

	public SessionProcedureController(SessionProcedureService sessionProcedureService) {
		this.sessionProcedureService = sessionProcedureService;
	}

	@PostMapping(value = "/v1/handler")
	public ResponseEntity<?> handler(HttpServletRequest request,
			@RequestBody SessionProcedureRequest sessionProcedureRequest) {
		try {
			return ResponseEntity.ok(sessionProcedureService.handler(request, sessionProcedureRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

}
