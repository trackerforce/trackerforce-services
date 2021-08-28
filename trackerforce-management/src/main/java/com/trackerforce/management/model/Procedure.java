package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonProcedure;

@Document(collection = "procedures")
public class Procedure extends CommonProcedure {
	
	private Hook hook;

	public Hook getHook() {
		return hook;
	}

	public void setHook(Hook hook) {
		this.hook = hook;
	}

}
