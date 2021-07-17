package com.trackerforce.identity.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.identity.model.request.JwtRequest;

@Document(collection = "auth-access")
public class AuthAccess extends AbstractIdentityDocument {
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String refreshToken;
	
	@JsonIgnore
	private String tokenHash;
	
	public AuthAccess() {}
	
	public AuthAccess(JwtRequest authRequest, Organization organization) {
		this.username = authRequest.getUsername();
		this.password = authRequest.getPassword();
		setOrganization(organization);
	}
	
	public boolean hasValidOrganization() {
		if (super.organization == null)
			return false;
		
		if (!StringUtils.hasText(super.organization.getName()))
			return false;
		
		return true;
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

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

}
