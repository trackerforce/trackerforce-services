package com.trackerforce.session.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SessionCaseRequest {

	@JsonProperty("cases")
	private String[] caseIds;

	@JsonProperty("agent_id")
	private String agentId;

	private String template;

}
