package com.trackerforce.management.model.type;

import com.trackerforce.management.service.exception.InvalidRenderTypeException;

public enum RenderType {

	MARKDOWN("Markdown"),
	HTML("HTML"),
	PLAINTEXT("Plain-text");
	
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