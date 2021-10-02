package com.trackerforce.identity.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.common.model.type.JwtKeys;
import com.trackerforce.common.model.type.ServicesRole;
import com.trackerforce.identity.model.request.JwtRequest;

@Document(collection = "auth-access")
public class AuthAccess extends AbstractIdentityDocument {
	
	private String email;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String refreshToken;
	
	@JsonIgnore
	private String tokenHash;
	
	@JsonIgnore
	private boolean root = true;
	
	public AuthAccess() {}
	
	public AuthAccess(JwtRequest authRequest, Organization organization) {
		this.email = authRequest.getEmail();
		this.password = authRequest.getPassword();
		setOrganization(organization);
	}
	
	/**
	 * Agents and Session users
	 */
	public AuthAccess(String username, String orgAlias) {
		this.email = username;
		setOrganizationByAlias(orgAlias);
		setRoot(false);
	}
	
	public boolean hasValidOrganization() {
		if (super.organization == null)
			return false;
		
		if (!StringUtils.hasText(super.organization.getName()))
			return false;
		
		return true;
	}
	
	@JsonIgnore
	public Map<String, Object> getDefaultClaims() {
		var claims = new HashMap<String, Object>();
		claims.put(JwtKeys.ROLES.toString(), Arrays.asList(ServicesRole.ROOT.name()));
		return claims;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

}
