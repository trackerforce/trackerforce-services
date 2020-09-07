package com.trackerforce.management.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "templates")
public class Template extends AbstractBusinessDocument {
	
	private String name;
	
	@DBRef
	private List<AbstractBusinessDocument> businessComponents = Collections.emptyList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AbstractBusinessDocument> getBusinessComponents() {
		return businessComponents;
	}

	public void setBusinessComponents(List<AbstractBusinessDocument> businessComponents) {
		this.businessComponents = businessComponents;
	}

}
