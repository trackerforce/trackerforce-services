package com.trackerforce.management.model;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.model.type.GlobalKeyType;

@Document(collection = "globals")
public class Global extends AbstractBusinessDocument {
	
	private GlobalKeyType key;
	
	private Map<String, Object> attributes;

	public GlobalKeyType getKey() {
		return key;
	}

	public void setKey(GlobalKeyType key) {
		this.key = key;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
