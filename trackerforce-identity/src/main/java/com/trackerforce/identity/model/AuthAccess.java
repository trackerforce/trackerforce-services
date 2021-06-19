package com.trackerforce.identity.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.identity.model.request.JwtRequest;
import com.trackerforce.identity.model.type.AccessType;

@Document(collection = "auth-access")
public class AuthAccess extends AbstractIdentityDocument {
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String refreshToken;
	
	@JsonIgnore
	private String tokenHash;
	
	private boolean owner;
	
	/**
	 * {@link AccessType}
	 */
	private String accessType;
	
	public AuthAccess() {}
	
	public AuthAccess(JwtRequest authRequest) {
		this.username = authRequest.getUsername();
		this.password = authRequest.getPassword();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

}
