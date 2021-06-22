package com.trackerforce.common.tenant.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

public abstract class AbstractProcedure extends AbstractBusinessDocument {
	
	/**
	 * Procedure topic name
	 */
	private String name;
	
	@DBRef
	private List<AbstractTask> tasks = Collections.emptyList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<AbstractTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<AbstractTask> childTasks) {
		this.tasks = childTasks;
	}

}
