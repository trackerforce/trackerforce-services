package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.trackerforce.common.model.AbstractDocument;

public abstract class AbstractBusinessDocument extends AbstractDocument {
	
	private String description;
	
	@DBRef
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
