package com.trackerforce.identity.model.type;

public enum AccessType {
	
	ORGANIZATION_USER("Organization User"),
	SESSION_USER("Session User"),
	APPLICATION("Application");
	
	private String name;
	
	private AccessType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
