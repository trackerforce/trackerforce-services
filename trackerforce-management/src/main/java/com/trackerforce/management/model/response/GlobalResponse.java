package com.trackerforce.management.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GlobalResponse {

	private String key;

	private String description;

	private String[] attributes;

	public GlobalResponse(String name, String description, String[] attributes) {
		this.key = name;
		this.description = description;
		this.attributes = attributes;
	}

}
