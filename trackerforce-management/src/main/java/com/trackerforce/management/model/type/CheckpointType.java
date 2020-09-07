package com.trackerforce.management.model.type;

public enum CheckpointType {
	
	MANUAL("Manual"),
	AUTO("Auto");
	
	private String name;
	
	private CheckpointType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
