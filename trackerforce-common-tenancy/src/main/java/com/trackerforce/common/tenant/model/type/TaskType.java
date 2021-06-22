package com.trackerforce.common.tenant.model.type;

import com.trackerforce.common.tenant.service.exception.InvalidTaskTypeException;

public enum TaskType {
	
	TEXT("Text"),
	MULTILINE_TEXT("MultilineText"),
	NUMBER("Number"),
	CHECK("Checkbox"),
	RADIO("Radio"),
	DRILLDOWN("Drilldown"),
	FILE("File");
	
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
