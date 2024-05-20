package com.trackerforce.management.model;

import java.util.LinkedList;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonProcedure;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "procedures")
@Data
public class Procedure extends CommonProcedure<Task> {
	
	@DBRef
	private LinkedList<Task> tasks;
	
	private Hook hook;

	@Override
	public LinkedList<Task> getTasks() {
		if (tasks == null)
			tasks = new LinkedList<>();
		return tasks;
	}

}
