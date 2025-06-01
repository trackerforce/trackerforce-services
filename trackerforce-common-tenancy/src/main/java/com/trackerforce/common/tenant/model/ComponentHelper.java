package com.trackerforce.common.tenant.model;

import com.trackerforce.common.tenant.model.type.RenderType;
import lombok.Data;

@Data
public class ComponentHelper {
	
	private String content;
	
	/**
	 * {@link RenderType}
	 */
	private String renderType;

}
