package com.trackerforce.common.tenant.model;

import java.util.Collections;
import java.util.List;

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
	protected List<AbstractTask> tasks = Collections.emptyList();

	public String getName() {
		return name;
	}
	
	public List<AbstractTask> getTasks() {
		return tasks;
	}

	public String getProcedureMngmtId() {
		return procedureMngmtId;
	}
	
}
