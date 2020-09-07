package com.trackerforce.identity.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.identity.model.type.AccessType;

@Document(collection = "auth-access")
public class AuthAccess extends AbstractIdentityDocument {
	
	private String userName;
	
	@JsonIgnore
	private String password;
	
	private String token;
	
	@JsonIgnore
	private String refreshToken;
	
	private boolean owner;
	
	/**
	 * {@link AccessType}
	 */
	private String accessType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

}
