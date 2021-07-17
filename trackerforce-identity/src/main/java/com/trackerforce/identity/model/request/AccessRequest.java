package com.trackerforce.identity.model.request;

import com.trackerforce.identity.model.AbstractIdentityDocument;
import com.trackerforce.identity.model.AuthAccess;

public class AccessRequest extends AbstractIdentityDocument {
	
	private String username;
	
	private String password;
	
	public AuthAccess getAuthAccess() {
		final var organization = getOrganization();
		organization.setAlias(organization.getName());
		
		final var authAccess = new AuthAccess();
		authAccess.setUsername(username);
		authAccess.setPassword(password);
		authAccess.setOrganization(organization);
		
		return authAccess;
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

}
