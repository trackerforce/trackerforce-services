package com.trackerforce.session.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionCaseRequest {

	@JsonProperty("cases")
	private String[] caseIds;

	@JsonProperty("agent_id")
	private String agentId;

	private String template;

	public String[] getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(String[] caseIds) {
		this.caseIds = caseIds;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
