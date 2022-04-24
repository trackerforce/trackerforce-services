package com.trackerforce.identity.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.model.response.AgentResponse;

@JsonInclude(Include.NON_NULL)
public class AuthAgentResponseDTO extends AuthResponseDTO {

	private AgentResponse access;
	
	public AuthAgentResponseDTO(AgentResponse access, String token, String refreshToken) {
		super(token, refreshToken, false);
		this.access = access;
	}

	public AgentResponse getAccess() {
		return access;
	}
	
	@Override
	public String getId() {
		return access.getId();
	}
	
}
