package com.trackerforce.management.model.request;

import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.management.model.Template;

public class TemplateRequest {
	
	private Template template;
	
	private ComponentHelper helper;

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template procedure) {
		this.template = procedure;
	}

	public ComponentHelper getHelper() {
		return helper;
	}

	public void setHelper(ComponentHelper helper) {
		this.helper = helper;
	}

}
