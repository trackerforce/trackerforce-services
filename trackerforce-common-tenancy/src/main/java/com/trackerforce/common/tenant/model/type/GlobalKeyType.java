package com.trackerforce.common.tenant.model.type;

public enum GlobalKeyType {

	ML_SERVICE("Machine Learning Engine Service URL", "url");
	
	private String summary;
	
	private String[] attributes;

	private GlobalKeyType(String summary, String... attributes) {
		this.summary = summary;
		this.attributes = attributes;
	}
	
	public boolean validate(String attribute) {
		for (String attrib : attributes)
			if (attrib.equals(attribute))
				return true;
		return false;
	}
	
	public String getSummary() {
		return this.summary;
	}

}