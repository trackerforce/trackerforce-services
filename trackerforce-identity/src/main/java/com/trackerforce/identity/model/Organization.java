package com.trackerforce.identity.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.model.AbstractDocument;

@Document(collection = "organizations")
public class Organization extends AbstractDocument {
	
	private String name;
	
	private boolean dedicated = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDedicated() {
		return dedicated;
	}

	public void setDedicated(boolean dedicated) {
		this.dedicated = dedicated;
	}

}
