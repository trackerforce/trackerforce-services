package com.trackerforce.management.model;

import java.util.LinkedList;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.model.CommonProcedure;

@Document(collection = "templates")
public class Template  extends AbstractBusinessDocument {
	
	private String name;
	
	private LinkedList<CommonProcedure> procedures;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<CommonProcedure> getProcedures() {
		if (procedures == null)
			procedures = new LinkedList<>();
		
		return procedures;
	}
	
}
