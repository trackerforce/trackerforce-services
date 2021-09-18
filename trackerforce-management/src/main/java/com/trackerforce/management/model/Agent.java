package com.trackerforce.management.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.type.ServicesRole;
import com.trackerforce.common.tenant.model.AbstractBusinessDocument;

@Document(collection = "agents")
public class Agent extends AbstractBusinessDocument {
	
	@JsonIgnore
	private String password;
	
	private String name;
	
	private String email;
	
	private boolean active = false;
	
	private boolean online = false;
	
	private List<String> roles;
	
	private List<String> cases;
	
	public Agent() {}
	
	public Agent(AgentRequest agentRequest) {
		setEmail(agentRequest.getEmail());
		setName(agentRequest.getName());
		setRoles(agentRequest.getRoles());
		setPassword(agentRequest.getPassword());
		setRoles(Arrays.asList(ServicesRole.AGENT.name()));
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getCases() {
		if (cases == null)
			cases = new ArrayList<>();
		return cases;
	}

	public void setCases(List<String> cases) {
		this.cases = cases;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
