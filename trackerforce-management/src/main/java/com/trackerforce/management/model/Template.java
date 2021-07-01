package com.trackerforce.management.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.model.AbstractProcedure;

@Document(collection = "templates")
public class Template  extends AbstractBusinessDocument {
	
	private String name;
	
	private List<AbstractProcedure> procedures;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AbstractProcedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(List<AbstractProcedure> procedures) {
		this.procedures = procedures;
	}
	
}
