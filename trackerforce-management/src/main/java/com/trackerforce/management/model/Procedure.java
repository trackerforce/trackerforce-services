package com.trackerforce.management.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "procedures")
public class Procedure extends AbstractBusinessDocument {
	
	/**
	 * Procedure topic name
	 */
	private String name;
	
	/**
	 * Generated code that is used to automated resolvers
	 */
	private String code;
	
	@DBRef
	private List<Task> tasks = Collections.emptyList();
	
	@DBRef
	private Checkpoint checkpoint;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> childTasks) {
		this.tasks = childTasks;
	}

	public Checkpoint getCheckpoint() {
		return checkpoint;
	}

	public void setCheckpoint(Checkpoint checkpoint) {
		this.checkpoint = checkpoint;
	}

}
