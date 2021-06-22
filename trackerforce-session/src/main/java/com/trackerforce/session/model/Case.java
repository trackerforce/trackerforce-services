package com.trackerforce.session.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *	Cases are business model that contains all necessary procedures and tasks to accomplish
 *	a single request.
 */
@Document(collection = "cases")
public class Case extends AbstractSessionDocument {
	
	private String protocol;
	
	private List<Object> procedures;

}
