package com.trackerforce.identity.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.identity.model.request.AccessRequest;
import com.trackerforce.identity.model.request.JwtRefreshRequest;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.service.AuthenticationService;

@CrossOrigin
@RestController
@RequestMapping("identity")
public class IdentityController {
	
	private final AuthenticationService authorizationService;
	
	public IdentityController(AuthenticationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	@PostMapping(value = "/v1/authenticate")
	public ResponseEntity<Map<String, Object>> authenticateRoot(@RequestBody JwtRequest authRequest) {
		return ResponseEntity.ok(authorizationService.authenticateAccess(authRequest));
	}
	
	@PostMapping(value = "/v1/refresh")
	public ResponseEntity<?> refreshAuth(HttpServletRequest request,
			@RequestBody JwtRefreshRequest authRefreshRequest) {
		try {
			return ResponseEntity.ok(authorizationService.authenticateRefreshAccess(request, authRefreshRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PostMapping(value = "/v1/register")
	public ResponseEntity<?> registerRoot(@RequestBody AccessRequest accessRequest) {
		return ResponseEntity.ok(authorizationService.registerAccess(accessRequest));
	}
	
	@PostMapping(value = "/v1/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, 
			HttpServletResponse response) {
		try {
			authorizationService.logoff(request, response);
			return ResponseEntity.ok().build();
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping(value = "/v1/me")
	public ResponseEntity<?> getAuthenticated(HttpServletRequest request) {
		try {
			return ResponseEntity.ok(authorizationService.getAuthenticated(request));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping(value = "/v1/valid")
	public ResponseEntity<?> isValid(HttpServletRequest request) {
		try {
			return ResponseEntity.ok(authorizationService.getAuthenticated(request) != null);
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

}
