package com.trackerforce.identity.model.dto.response;

import org.apache.commons.lang3.StringUtils;

public class AuthResponseDTO {
	
	protected String token;

	protected String refreshToken;
	
	protected Boolean root;

	public AuthResponseDTO(String token, String refreshToken, Boolean root) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.root = root;
	}

	public String getToken() {
		return token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Boolean isRoot() {
		return root;
	}
	
	public String getId() {
		return StringUtils.EMPTY;
	}
	
}
