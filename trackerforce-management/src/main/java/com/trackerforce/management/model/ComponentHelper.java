package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.management.model.type.RenderType;

@Document(collection = "helpers")
public class ComponentHelper extends AbstractBusinessDocument {
	
	private String content;
	
	/**
	 * {@link RenderType}
	 */
	private String renderType;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}

}
