package com.trackerforce.common.tenant.model;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CommonTemplate<T extends CommonProcedure<? extends CommonTask>> extends AbstractBusinessDocument {

	private String name;

	private LinkedList<T> procedures;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<T> getProcedures() {
		if (procedures == null)
			procedures = new LinkedList<>();

		return procedures;
	}

}
