package com.trackerforce.management.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "procedures")
public class Procedure extends AbstractBusinessDocument {
	
	private String name;
	
	@DBRef
	private List<Procedure> childProcedures = Collections.emptyList();
	
	@DBRef
	private List<Task> childTasks = Collections.emptyList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Procedure> getChildProcedures() {
		return childProcedures;
	}

	public void setChildProcedures(List<Procedure> childProcedures) {
		this.childProcedures = childProcedures;
	}

	public List<Task> getChildTasks() {
		return childTasks;
	}

	public void setChildTasks(List<Task> childTasks) {
		this.childTasks = childTasks;
	}

}
