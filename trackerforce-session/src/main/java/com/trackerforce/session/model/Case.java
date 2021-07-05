package com.trackerforce.session.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractProcedure;

/**
 *	Cases are business model that contains all necessary procedures and tasks to accomplish
 *	a single user/customer request.
 */
@Document(collection = "cases")
public class Case extends AbstractSessionDocument {
	
	private String protocol;
	
	private List<AbstractProcedure> procedures;

}
