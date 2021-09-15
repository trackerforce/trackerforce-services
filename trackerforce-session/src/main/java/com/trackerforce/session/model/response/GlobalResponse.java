package com.trackerforce.session.model.response;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class GlobalResponse {

	private String key;

	private String description;

	private Map<String, Object> attributes;

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

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public String getValue(String attributeKey) {
		return attributes.containsKey(attributeKey) ? attributes.get(attributeKey).toString() : StringUtils.EMPTY;
	}

}
