package com.trackerforce.session.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(methods = { RequestMethod.POST })
@RestController
@RequestMapping("session")
public class SessionController {
	
	@PostMapping(value = "/v1/create")
	public ResponseEntity<?> create() {
		//TODO
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/v1/procedure/submit")
	public ResponseEntity<?> submitProcedure() {
		//TODO
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/v1/procedure/select")
	public ResponseEntity<?> selectProcedure() {
		//TODO
		return ResponseEntity.ok().build();
	}

}
