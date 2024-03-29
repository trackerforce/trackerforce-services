package com.trackerforce.identity.model;

public class Organization {

	private String name;

	private String alias;

	public Organization() {
	}

	public Organization(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
