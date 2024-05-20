package com.trackerforce.management.model.request;

import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.management.model.Template;
import lombok.Data;

@Data
public class TemplateRequest {
	
	private Template template;
	
	private ComponentHelper helper;

}
