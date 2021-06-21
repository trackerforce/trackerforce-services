package com.trackerforce.management.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.management.model.type.TaskType;

@Document(collection = "tasks")
public class Task extends AbstractBusinessDocument {
	
	/**
	 * {@link TaskType}
	 */
	private String type;
	
	private List<TaskOption> options;

	public Task(String type, String description, List<TaskOption> options) {
		setDescription(description);
		this.type = type;
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TaskOption> getOptions() {
		return options;
	}

	public void setOptions(List<TaskOption> options) {
		this.options = options;
	}

}
