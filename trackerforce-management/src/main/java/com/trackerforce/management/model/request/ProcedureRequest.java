package com.trackerforce.management.model.request;

import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.management.model.Procedure;
import lombok.Data;

@Data
public class ProcedureRequest {
	
	private Procedure procedure;
	
	private ComponentHelper helper;

}
