package com.trackerforce.management.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.type.ServicesRole;
import com.trackerforce.common.tenant.model.AbstractBusinessDocument;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "agents")
@Data
@NoArgsConstructor
public class Agent extends AbstractBusinessDocument {
	
	@JsonIgnore
	private String password;
	
	private String name;
	
	private String email;
	
	private boolean active = false;
	
	private boolean online = false;
	
	private List<String> roles;
	
	private List<String> cases;

	public Agent(AgentRequest agentRequest) {
		setEmail(agentRequest.getEmail());
		setName(agentRequest.getName());
		setRoles(agentRequest.getRoles());
		setPassword(agentRequest.getPassword());
		setRoles(List.of(ServicesRole.AGENT.name()));
	}

	public List<String> getCases() {
		if (cases == null)
			cases = new ArrayList<>();
		return cases;
	}

}
