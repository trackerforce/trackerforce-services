package com.trackerforce.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.TokenResponse;
import com.trackerforce.common.service.JwtTokenService;

@CrossOrigin
@RestController
@RequestMapping("service")
public class ServiceCommon {
	
	@Autowired
	private JwtTokenService jwtTokenUtil;

	@RequestMapping(value = "/check")
	public ResponseEntity<String> check() {
		return ResponseEntity.ok("All good");
	}
	
	@Deprecated
	@RequestMapping(value = "/check/auth")
	public ResponseEntity<TokenResponse> auth(@RequestParam("user") String user) {
		if (!StringUtils.hasText(user)) {
			return ResponseEntity.badRequest().build();
		}
		
		final TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(jwtTokenUtil.generateToken(user));
		return ResponseEntity.ok(tokenResponse);
	}

}
