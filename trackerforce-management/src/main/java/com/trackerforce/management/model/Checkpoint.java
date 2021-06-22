package com.trackerforce.management.model;

/**
 *	Checkpoints are triggered every time a procedure is updated.
 *	The componentes filled such as Agent, Department and Resolver are
 *	notified when this event occurs.
 */
public class Checkpoint {
	
	private String agentId;
	
	private String departmentId;
	
	private String resolverUri;
	
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
