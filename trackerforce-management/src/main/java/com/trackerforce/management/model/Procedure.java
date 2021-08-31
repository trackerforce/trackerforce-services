package com.trackerforce.management.model;

import java.util.LinkedList;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonProcedure;

@Document(collection = "procedures")
public class Procedure extends CommonProcedure<Task> {
	
	@DBRef
	private LinkedList<Task> tasks;
	
	private Hook hook;

	public Hook getHook() {
		return hook;
	}

	public void setHook(Hook hook) {
		this.hook = hook;
	}

	@Override
	public LinkedList<Task> getTasks() {
		if (tasks == null)
			tasks = new LinkedList<Task>();
		return tasks;
	}

}
