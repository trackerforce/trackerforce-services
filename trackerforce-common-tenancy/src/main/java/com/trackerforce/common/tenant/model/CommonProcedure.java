package com.trackerforce.common.tenant.model;

import java.util.LinkedList;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class CommonProcedure extends AbstractBusinessDocument {
	
	/**
	 * Procedure topic name
	 */
	protected String name;
	
	@DBRef
	protected LinkedList<CommonTask> tasks;

	public String getName() {
		return name;
	}
	
	public LinkedList<CommonTask> getTasks() {
		if (tasks == null)
			tasks = new LinkedList<>();
		
		return tasks;
	}

}
