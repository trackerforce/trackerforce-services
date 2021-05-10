package com.trackerforce.identity.model.request;

import com.trackerforce.identity.model.AbstractIdentityDocument;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.Organization;
import com.trackerforce.identity.model.type.AccessType;

public class AccessRequest extends AbstractIdentityDocument {
	
	private String username;
	
	private String password;
	
	private String organizationName;
	
	private boolean dedicated = false;
	
	private boolean owner;
	
	/**
	 * {@link AccessType}
	 */
	private String accessType;
	
	public AuthAccess getAuthAccess() {
		final var authAccess = new AuthAccess();
		authAccess.setUsername(username);
		authAccess.setPassword(password);
		authAccess.setOwner(owner);
		
		final var organization = new Organization();
		organization.setName(organizationName);
		organization.setDedicated(dedicated);
		authAccess.setOrganization(getOrganization());
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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public boolean isDedicated() {
		return dedicated;
	}

	public void setDedicated(boolean dedicated) {
		this.dedicated = dedicated;
	}

}
