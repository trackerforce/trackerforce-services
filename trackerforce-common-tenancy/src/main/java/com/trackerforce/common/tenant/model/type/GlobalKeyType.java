package com.trackerforce.common.tenant.model.type;

public enum GlobalKeyType {

	ML_SERVICE("Machine Learning Engine Service", "url", "accuracy"),
	AGENT_PLAN("Agent subscription", "quantity");

	private String description;

	private String[] attributes;

	private GlobalKeyType(String description, String... attributes) {
		this.description = description;
		this.attributes = attributes;
	}

	public boolean validate(String attribute) {
		for (String attrib : attributes)
			if (attrib.equals(attribute))
				return true;
		return false;
	}

	public String getDescription() {
		return this.description;
	}

	public String[] getAttributes() {
		return attributes;
	}

}