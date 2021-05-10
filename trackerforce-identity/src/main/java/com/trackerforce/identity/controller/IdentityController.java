package com.trackerforce.identity.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.identity.model.request.AccessRequest;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.service.AuthenticationService;

@CrossOrigin
@RestController
@RequestMapping("identity")
public class IdentityController {
	
	@Autowired
	AuthenticationService authorizationService;
	
	@PostMapping(value = "/v1/authenticate")
	public ResponseEntity<Map<String, Object>> authenticate(@RequestBody JwtRequest authRequest) {
		return ResponseEntity.ok(authorizationService.authenticateAccess(authRequest));
	}
	
	@PostMapping(value = "/v1/register")
	public ResponseEntity<?> saveUser(@RequestBody AccessRequest accessRequest) {
		return ResponseEntity.ok(authorizationService.registerAccess(accessRequest));
	}

}
