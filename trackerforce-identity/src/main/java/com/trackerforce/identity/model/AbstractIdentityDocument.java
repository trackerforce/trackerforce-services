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
	
	public void setOrganizationByAlias(String orgAlias) {
		this.organization = new Organization(orgAlias);
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
