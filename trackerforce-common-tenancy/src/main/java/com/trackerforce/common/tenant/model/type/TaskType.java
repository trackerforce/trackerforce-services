package com.trackerforce.common.tenant.model.type;

import com.trackerforce.common.tenant.service.exception.InvalidTaskTypeException;

public enum TaskType {
	
	TEXT("TEXT", false),
	MULTILINE_TEXT("MULTILINE_TEXT", false),
	NUMBER("NUMBER", true),
	CHECK("CHECK", true),
	RADIO("RADIO", true),
	DRILLDOWN("DRILLDOWN", true),
	FILE("FILE", false);

	private String name;

	private boolean learnable;

	private TaskType(String name, boolean learnable) {
		this.name = name;
		this.learnable = learnable;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public boolean isLearnable() {
		return learnable;
	}

	public static void validate(String type) throws InvalidTaskTypeException {
		try {
			TaskType.valueOf(type);
		} catch (Exception e) {
			throw new InvalidTaskTypeException(type, e);
		}
	}

}
