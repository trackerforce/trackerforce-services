package com.trackerforce.common.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AgentResponse {

	private String id;

	private String name;

	private String email;

	@JsonProperty("temp_access")
	private String tempAccess;

	private List<String> roles;

	private List<String> cases;

	private boolean online;

	private boolean active;

	public static AgentResponse createAgent(String name, String email, String password, List<String> roles) {
		final var agent = new AgentResponse();
		agent.setName(name);
		agent.setEmail(email);
		agent.setRoles(roles);
		agent.setTempAccess(password);
		return agent;
	}

	public static AgentResponse login(String id, String email, List<String> roles) {
		final var agent = new AgentResponse();
		agent.setId(id);
		agent.setEmail(email);
		agent.setRoles(roles);
		agent.setOnline(true);
		agent.setActive(true);
		return agent;
	}

	public static AgentResponse auth(String email, List<String> roles, boolean online) {
		final var agent = new AgentResponse();
		agent.setEmail(email);
		agent.setRoles(roles);
		agent.setOnline(online);
		agent.setActive(true);
		return agent;
	}

	public static AgentResponse watch(String email, List<String> cases) {
		final var agent = new AgentResponse();
		agent.setEmail(email);
		agent.setCases(cases);
		agent.setOnline(true);
		agent.setActive(true);
		return agent;
	}

}
