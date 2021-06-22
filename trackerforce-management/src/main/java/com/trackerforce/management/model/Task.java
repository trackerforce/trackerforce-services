package com.trackerforce.management.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractTask;
import com.trackerforce.common.tenant.model.TaskOption;

@Document(collection = "tasks")
public class Task extends AbstractTask {

	public Task(String type, String description, List<TaskOption> options) {
		super(type, description, options);
	}

}
