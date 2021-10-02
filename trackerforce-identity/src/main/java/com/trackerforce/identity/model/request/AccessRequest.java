package com.trackerforce.identity.model.request;

import com.trackerforce.identity.model.AbstractIdentityDocument;
import com.trackerforce.identity.model.AuthAccess;

public class AccessRequest extends AbstractIdentityDocument {

	private String email;

	private String password;

	public AuthAccess getAuthAccess() {
		final var organization = getOrganization();
		organization.setAlias(organization.getName());

		final var authAccess = new AuthAccess();
		authAccess.setEmail(email);
		authAccess.setPassword(password);
		authAccess.setOrganization(organization);

		return authAccess;
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

}
