package com.trackerforce.session.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.session.model.request.SessionProcedureRequest;

@CrossOrigin(methods = { RequestMethod.POST })
@RestController
@RequestMapping("session/procedure")
public class SessionProcedureController {

	@PostMapping(value = "/v1/handler")
	public ResponseEntity<?> hanlder(HttpServletRequest request,
			@RequestBody SessionProcedureRequest sessionProcedureRequest) {
		return ResponseEntity.ok().build();
	}

}
