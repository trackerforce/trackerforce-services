package com.trackerforce.common.tenant.model;

import java.util.LinkedList;

import org.springframework.data.mongodb.core.mapping.DBRef;

public abstract class AbstractProcedure extends AbstractBusinessDocument {
	
	/**
	 * Procedure topic name
	 */
	protected String name;
	
	/**
	 * Link to Procedure Management details.
	 * Session cases can only have access to the procedure in/out and
	 * all the routing configurations are placed at te Procedure Management component.
	 */
	protected String procedureMngmtId;
	
	@DBRef
	protected LinkedList<AbstractTask> tasks;

	public String getName() {
		return name;
	}
	
	public LinkedList<AbstractTask> getTasks() {
		if (tasks == null)
			tasks = new LinkedList<>();
		
		return tasks;
	}

	public String getProcedureMngmtId() {
		return procedureMngmtId;
	}
	
}
