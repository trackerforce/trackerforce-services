package com.trackerforce.identity.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.identity.model.AuthAccess;

@JsonInclude(Include.NON_NULL)
public class AuthAgentResponseDTO extends AuthResponseDTO {

	private AgentResponse access;
	
	public AuthAgentResponseDTO(AgentResponse access, String token, String refreshToken) {
		super(token, refreshToken, false);
		this.access = access;
	}
	
	public AuthAgentResponseDTO(AuthAccess access, List<String> roles, boolean online) {
		super(null, null, false);
		this.access = AgentResponse.auth(access.getEmail(), roles, online);
	}

	public AgentResponse getAccess() {
		return access;
	}
	
	@Override
	public String getId() {
		return access.getId();
	}
	
}
