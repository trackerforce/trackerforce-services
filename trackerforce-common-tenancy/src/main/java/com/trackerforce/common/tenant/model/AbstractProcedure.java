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
	 * Link to Procedure Management details
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
