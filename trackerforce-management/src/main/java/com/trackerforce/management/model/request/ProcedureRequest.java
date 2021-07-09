package com.trackerforce.management.model.request;

import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.management.model.Procedure;

public class ProcedureRequest {
	
	private Procedure procedure;
	
	private ComponentHelper helper;

	public Procedure getProcedure() {
		return procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	public ComponentHelper getHelper() {
		return helper;
	}

	public void setHelper(ComponentHelper helper) {
		this.helper = helper;
	}

}
