package com.trackerforce.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.trackerforce.common.model.response.TokenResponse;
import com.trackerforce.common.service.JwtTokenService;

@CrossOrigin
@RestController
@RequestMapping("service")
public class ServiceCommon {
	
	@Autowired
	private JwtTokenService jwtTokenUtil;

	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ResponseEntity<String> check() {
		return ResponseEntity.ok("All good");
	}
	
	@Deprecated
	@RequestMapping(method = RequestMethod.POST, value = "/check/auth")
	public ResponseEntity<TokenResponse> auth(@RequestParam("user") String user) {
		if (!StringUtils.hasText(user)) {
			return ResponseEntity.badRequest().build();
		}
		
		final TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(jwtTokenUtil.generateToken(user, "test-org", null)[0]);
		return ResponseEntity.ok(tokenResponse);
	}

}
