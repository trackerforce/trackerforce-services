package com.trackerforce.identity.model.dto.request;

public class JwtRefreshRequestDTO {

	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}