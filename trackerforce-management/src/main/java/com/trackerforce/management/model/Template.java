package com.trackerforce.management.model;

import java.util.LinkedList;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonTemplate;

@Document(collection = "templates")
public class Template extends CommonTemplate<Procedure> {
	
	@DBRef
	private LinkedList<Procedure> procedures;
	
	@Override
	public LinkedList<Procedure> getProcedures() {
		if (procedures == null)
			procedures = new LinkedList<Procedure>();
		return procedures;
	}

}
