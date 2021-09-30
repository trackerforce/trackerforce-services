package com.trackerforce.common.tenant.model;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public abstract class CommonTemplate<T extends CommonProcedure<? extends CommonTask>> extends AbstractBusinessDocument {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public abstract LinkedList<T> getProcedures();

}
