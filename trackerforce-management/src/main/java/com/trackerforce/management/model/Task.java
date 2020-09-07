package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.management.model.type.TaskType;

@Document(collection = "tasks")
public class Task extends AbstractBusinessDocument {
	
	/**
	 * {@link TaskType}
	 */
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
