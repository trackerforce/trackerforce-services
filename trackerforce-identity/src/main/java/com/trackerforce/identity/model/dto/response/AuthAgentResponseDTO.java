package com.trackerforce.identity.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.identity.model.AuthAccess;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(Include.NON_NULL)
public class AuthAgentResponseDTO extends AuthResponseDTO {

	private final AgentResponse access;
	
	public AuthAgentResponseDTO(AgentResponse access, String token, String refreshToken) {
		super(token, refreshToken, false);
		this.access = access;
	}
	
	public AuthAgentResponseDTO(AuthAccess access, List<String> roles, boolean online) {
		super(null, null, false);
		this.access = AgentResponse.auth(access.getEmail(), roles, online);
	}

    @Override
	public String getId() {
		return access.getId();
	}
	
}
