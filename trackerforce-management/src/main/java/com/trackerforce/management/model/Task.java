package com.trackerforce.management.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.common.tenant.model.TaskOption;
import com.trackerforce.common.tenant.model.type.TaskType;

@Document(collection = "tasks")
public class Task extends CommonTask {

	public Task(TaskType type, String description, List<TaskOption> options) {
		super(type, description, options);
	}

}
