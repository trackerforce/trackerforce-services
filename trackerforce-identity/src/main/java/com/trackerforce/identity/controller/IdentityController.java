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

import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.request.AccessRequest;
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
	
	@PostMapping(value = "/v1/register")
	public ResponseEntity<?> registerRoot(@RequestBody AccessRequest accessRequest) {
		return ResponseEntity.ok(authorizationService.registerAccess(accessRequest));
	}
	
	@PostMapping(value = "/v1/logout")
	public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
		authorizationService.logoff(request, response);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value = "/v1/me")
	public ResponseEntity<AuthAccess> getAuthenticated(HttpServletRequest request) {
		return ResponseEntity.ok(authorizationService.getAuthenticated(request));
	}
	
	@GetMapping(value = "/v1/valid")
	public ResponseEntity<Boolean> isValid(HttpServletRequest request) {
		return ResponseEntity.ok(authorizationService.getAuthenticated(request) != null);
	}

}
