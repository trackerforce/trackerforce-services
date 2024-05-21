package com.trackerforce.session.model.response;

import java.util.Map;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class GlobalResponse {

	private String key;

	private String description;

	private Map<String, Object> attributes;
	
	public String getValue(String attributeKey) {
		return attributes.containsKey(attributeKey) ? attributes.get(attributeKey).toString() : StringUtils.EMPTY;
	}

}
