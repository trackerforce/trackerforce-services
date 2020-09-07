package com.trackerforce.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.service.AuthenticationService;

@CrossOrigin
@RestController
@RequestMapping("identity")
public class IdentityController {
	
	@Autowired
	private AuthenticationService authorizationService;
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
		try {
			AuthAccess authAccess = authorizationService.authenticateAccess(authRequest);
			return ResponseEntity.ok(authAccess);
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

}
