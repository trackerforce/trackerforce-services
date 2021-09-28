package com.trackerforce.management.model.response;

public class GlobalResponse {

	private String key;

	private String summary;

	private String[] attrbutes;

	public GlobalResponse(String name, String summary, String[] attributes) {
		this.key = name;
		this.summary = summary;
		this.attrbutes = attributes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String[] getAttrbutes() {
		return attrbutes;
	}

	public void setAttrbutes(String[] attrbutes) {
		this.attrbutes = attrbutes;
	}

}
