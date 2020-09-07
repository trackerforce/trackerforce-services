package com.trackerforce.management.model;

import java.util.Collections;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.common.model.AbstractDocument;

@Document(collection = "agents")
public class Agent extends AbstractDocument {
	
	private String name;
	
	private String email;
	
	private String department;
	
	private Binary avatar;
	
	private boolean active = true;
	
	private List<String> roles =  Collections.emptyList();
	
	@JsonIgnore
	private String password;

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

	public Binary getAvatar() {
		return avatar;
	}

	public void setAvatar(Binary avatar) {
		this.avatar = avatar;
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
