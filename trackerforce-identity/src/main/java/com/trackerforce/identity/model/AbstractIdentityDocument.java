package com.trackerforce.identity.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.trackerforce.common.model.AbstractDocument;

public abstract class AbstractIdentityDocument extends AbstractDocument {
	
	@DBRef
	private Organization organization;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
