package com.trackerforce.identity.model.dto.request;

import com.trackerforce.identity.model.Organization;

public class AccessRequestDTO {

	private String email;

	private String password;
	
	private Organization organization;

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
	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
