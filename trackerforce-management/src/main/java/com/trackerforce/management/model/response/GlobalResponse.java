package com.trackerforce.management.model.response;

public class GlobalResponse {

	private String key;

	private String description;

	private String[] attributes;

	public GlobalResponse(String name, String description, String[] attributes) {
		this.key = name;
		this.description = description;
		this.attributes = attributes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getAttributes() {
		return attributes;
	}

	public void setAttributes(String[] attrbutes) {
		this.attributes = attrbutes;
	}

}
