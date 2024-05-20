package com.trackerforce.management.model.request;

import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.management.model.Task;
import lombok.Data;

@Data
public class TaskRequest {
	
	private Task task;
	
	private ComponentHelper helper;

}
