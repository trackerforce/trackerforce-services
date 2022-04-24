package com.trackerforce.common.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTempAccess() {
		return tempAccess;
	}

	public void setTempAccess(String tempAccess) {
		this.tempAccess = tempAccess;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getCases() {
		return cases;
	}

	public void setCases(List<String> cases) {
		this.cases = cases;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
