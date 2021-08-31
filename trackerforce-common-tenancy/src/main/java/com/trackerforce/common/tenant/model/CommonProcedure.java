package com.trackerforce.common.tenant.model;

import java.util.LinkedList;

public abstract class CommonProcedure<T extends CommonTask> extends AbstractBusinessDocument {
	
	/**
	 * Procedure topic name
	 */
	protected String name;

	public String getName() {
		return name;
	}
	
	public abstract LinkedList<T> getTasks();

}
