package com.trackerforce.management.model.type;

import com.trackerforce.management.service.exception.InvalidTaskTypeException;

public enum TaskType {
	
	FILE("File"),
	MULTIFILE("Multi-file"),
	CHECK("Checkbox"),
	MULTISELECTION("Multi-selection");
	
	private String name;
	
	private TaskType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static void validate(String type) throws InvalidTaskTypeException {
		try {
			TaskType.valueOf(type);
		} catch (Exception e) {
			throw new InvalidTaskTypeException(type, e);
		}
	}

}
