package com.trackerforce.common.tenant.model.type;

import com.trackerforce.common.tenant.service.exception.InvalidRenderTypeException;

public enum RenderType {

	MARKDOWN("MARKDOWN"),
	HTML("HTML"),
	PLAINTEXT("PLAINTEXT");
	
	private String name;
	
	private RenderType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static void validate(String type) throws InvalidRenderTypeException {
		try {
			RenderType.valueOf(type);
		} catch (Exception e) {
			throw new InvalidRenderTypeException(type, e);
		}
	}

}