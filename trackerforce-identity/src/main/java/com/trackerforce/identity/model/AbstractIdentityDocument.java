package com.trackerforce.identity.model;

import com.trackerforce.common.model.AbstractDocument;

public abstract class AbstractIdentityDocument extends AbstractDocument {
	
	protected Organization organization;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
