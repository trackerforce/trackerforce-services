package com.trackerforce.identity.model;

import com.trackerforce.common.model.AbstractDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractIdentityDocument extends AbstractDocument {
	
	protected Organization organization;
	
	public void setOrganizationByAlias(String orgAlias) {
		this.organization = new Organization(orgAlias);
	}

}
