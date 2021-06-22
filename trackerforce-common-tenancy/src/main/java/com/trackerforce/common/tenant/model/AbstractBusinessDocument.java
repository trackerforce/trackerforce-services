package com.trackerforce.common.tenant.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.model.AbstractDocument;

@JsonInclude(Include.NON_NULL)
public abstract class AbstractBusinessDocument extends AbstractDocument {
	
	private String description;
	
	private ComponentHelper helper;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ComponentHelper getHelper() {
		return helper;
	}

	public void setHelper(ComponentHelper helper) {
		this.helper = helper;
	}

}
