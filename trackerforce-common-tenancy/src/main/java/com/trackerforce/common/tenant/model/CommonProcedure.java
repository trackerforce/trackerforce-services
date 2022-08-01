package com.trackerforce.common.tenant.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class CommonProcedure<T extends CommonTask> extends AbstractBusinessDocument {
	
	/**
	 * Procedure topic name
	 */
	protected String name;
	
	public abstract List<T> getTasks();

}
