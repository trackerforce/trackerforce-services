package com.trackerforce.management.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.tenant.model.AbstractBusinessDocument;

@Document(collection = "agents")
public class Agent extends AbstractBusinessDocument {
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String refreshToken;
	
	@JsonIgnore
	private String tokenHash;
	
	private String name;
	
	private String email;
	
	private String department;
	
	private boolean active = false;
	
	private List<String> roles =  Collections.emptyList();
	
	public Agent() {}
	
	public Agent(AgentRequest agentRequest) {
		setDepartment(agentRequest.getDepartment());
		setEmail(agentRequest.getEmail());
		setName(agentRequest.getName());
		setRoles(agentRequest.getRoles());
		setPassword(agentRequest.getPassword());
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
