package com.trackerforce.management.model.type;

public enum CheckpointType {
	
	/**
	 * No validation are executed and the next procedure is loaded
	 */
	NONE("None"),
	
	/**
	 * Procedure is set to pause for manual validation
	 */
	MANUAL("Manual"),
	
	/**
	 * Procedure triggers automation to resolve tasks
	 */
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
