package com.trackerforce.management.model.request;

import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.management.model.Global;

public class GlobalRequest {
	
	private Global global;
	
	private ComponentHelper helper;

	public Global getGlobal() {
		return global;
	}

	public void setGlobal(Global global) {
		this.global = global;
	}

	public ComponentHelper getHelper() {
		return helper;
	}

	public void setHelper(ComponentHelper helper) {
		this.helper = helper;
	}

}
