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

	private String department;

	@JsonProperty("temp_access")
	private String tempAccess;

	private List<String> roles;

	private List<String> cases;

	private boolean online;

	private boolean active;

	public AgentResponse() {
	}

	private AgentResponse(String name, String email, String password, List<String> roles) {
		setName(name);
		setEmail(email);
		setRoles(roles);
		setTempAccess(password);
	}

	private AgentResponse(String id, String email, List<String> roles, boolean online, boolean active) {
		setId(id);
		setEmail(email);
		setRoles(roles);
		setOnline(online);
		setActive(active);
	}

	private AgentResponse(String email, List<String> cases) {
		setEmail(email);
		setCases(cases);
	}

	public static AgentResponse createAgent(String name, String email, String password, List<String> roles) {
		return new AgentResponse(name, email, password, roles);
	}

	public static AgentResponse login(String id, String email, List<String> roles, boolean online, boolean active) {
		return new AgentResponse(id, email, roles, online, active);
	}

	public static AgentResponse watch(String email, List<String> cases) {
		return new AgentResponse(email, cases);
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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
