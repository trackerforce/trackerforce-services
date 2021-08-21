package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonProcedure;

@Document(collection = "procedures")
public class Procedure extends CommonProcedure {
	
	private Checkpoint checkpoint;
	
	public Checkpoint getCheckpoint() {
		return checkpoint;
	}

	public void setCheckpoint(Checkpoint checkpoint) {
		this.checkpoint = checkpoint;
	}

}
