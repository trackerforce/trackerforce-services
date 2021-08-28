package com.trackerforce.session.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trackerforce.session.model.SessionTask;
import com.trackerforce.session.model.type.SessionProcedureEvent;

public class SessionProcedureRequest {

	private SessionProcedureEvent event;

	@JsonProperty("case")
	private String sessionCaseId;

	@JsonProperty("procedure")
	private String procedureId;

	@JsonProperty("agent_id")
	private String agentId;

	private List<SessionTask> tasks;

	public SessionProcedureEvent getEvent() {
		return event;
	}

	public void setEvent(SessionProcedureEvent event) {
		this.event = event;
	}

	public String getSessionCaseId() {
		return sessionCaseId;
	}

	public void setSessionCaseId(String sessionCaseId) {
		this.sessionCaseId = sessionCaseId;
	}

	public String getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public List<SessionTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<SessionTask> tasks) {
		this.tasks = tasks;
	}

}
