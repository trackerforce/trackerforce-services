package com.trackerforce.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.management.model.type.CheckpointType;

@Document(collection = "checkpoints")
public class Checkpoint extends AbstractBusinessDocument {
	
	/**
	 * {@link CheckpointType}
	 */
	private String type;
	
	/**
	 * When Manual, an Agent is notified
	 */
	private String agentId;
	
	/**
	 * When Manual, a Department is notified
	 */
	private String departmentId;
	
	/**
	 * When Auto, a Resolver is triggered with a given payload
	 */
	private String resolverUri;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getResolverUri() {
		return resolverUri;
	}

	public void setResolverUri(String resolverUri) {
		this.resolverUri = resolverUri;
	}

}
