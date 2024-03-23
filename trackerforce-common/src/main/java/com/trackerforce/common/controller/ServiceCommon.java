package com.trackerforce.common.controller;

import com.trackerforce.common.model.response.TokenResponse;
import com.trackerforce.common.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("service")
public class ServiceCommon {

	private final JwtTokenService jwtTokenUtil;

	public ServiceCommon(final JwtTokenService jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@GetMapping("/check")
	public ResponseEntity<Map<String, String>> check() {
		return ResponseEntity.ok(Map.of("status", "All good"));
	}

	@PostMapping("/check/auth")
	public ResponseEntity<TokenResponse> auth(@RequestParam("user") String user) {
		if (!StringUtils.hasText(user)) {
			return ResponseEntity.badRequest().build();
		}
		
		final TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(jwtTokenUtil.generateToken(user, List.of("test-org"), null).getLeft());
		return ResponseEntity.ok(tokenResponse);
	}

}
