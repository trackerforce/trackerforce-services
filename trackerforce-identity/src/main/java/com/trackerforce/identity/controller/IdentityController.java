package com.trackerforce.identity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.identity.model.dto.request.AccessRequestDTO;
import com.trackerforce.identity.model.dto.request.JwtRefreshRequestDTO;
import com.trackerforce.identity.model.dto.request.JwtRequestDTO;
import com.trackerforce.identity.model.dto.response.AuthRootResponseDTO;
import com.trackerforce.identity.model.dto.response.AuthResponseDTO;
import com.trackerforce.identity.service.AuthenticationService;

@CrossOrigin
@RestController
@RequestMapping("identity/v1")
public class IdentityController {
	
	private final AuthenticationService authorizationService;
	
	public IdentityController(AuthenticationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<AuthRootResponseDTO> authenticateRoot(@RequestBody JwtRequestDTO authRequest) {
		return ResponseEntity.ok(authorizationService.authenticateRootAccess(authRequest));
	}
	
	@PostMapping(value = "/refresh")
	public ResponseEntity<AuthResponseDTO> refreshAuth(HttpServletRequest request,
			@RequestBody JwtRefreshRequestDTO authRefreshRequest) {
		return ResponseEntity.ok(authorizationService.authenticateRefreshAccess(request, authRefreshRequest));
	}
	
	@PostMapping(value = "/register")
	public ResponseEntity<AuthRootResponseDTO> registerRoot(@RequestBody AccessRequestDTO accessRequest) {
		return ResponseEntity.ok(authorizationService.registerAccess(accessRequest));
	}
	
	@PostMapping(value = "/logout")
	public ResponseEntity<Object> logout(HttpServletRequest request, 
			HttpServletResponse response) {
		authorizationService.logoff(request, response);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value = "/me", headers = "X-Tenant")
	public ResponseEntity<AuthResponseDTO> getAuthenticated(HttpServletRequest request) {
		return ResponseEntity.ok(authorizationService.getAuthenticated(request));
	}
	
	@GetMapping(value = "/valid")
	public ResponseEntity<Boolean> isValid(HttpServletRequest request) {
		return ResponseEntity.ok(authorizationService.getAuthenticated(request) != null);
	}

}
