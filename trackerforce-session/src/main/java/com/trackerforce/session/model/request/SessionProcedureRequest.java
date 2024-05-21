package com.trackerforce.session.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trackerforce.session.model.SessionTask;
import com.trackerforce.session.model.type.SessionProcedureEvent;
import lombok.Data;

@Data
public class SessionProcedureRequest {

	private SessionProcedureEvent event;

	@JsonProperty("case")
	private String sessionCaseId;

	@JsonProperty("procedure")
	private String procedureId;

	@JsonProperty("agent_id")
	private String agentId;

	private String resolution;

	private List<SessionTask> tasks;

}
