package com.trackerforce.common.model.response;

import java.util.Collections;
import java.util.List;

public class AgentResponse {
	
	private String name;
	
	private String email;
	
	private String department;
	
	private String temp_access;
	
	private List<String> roles =  Collections.emptyList();

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

	public String getTemp_access() {
		return temp_access;
	}

	public void setTemp_access(String temp_access) {
		this.temp_access = temp_access;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
