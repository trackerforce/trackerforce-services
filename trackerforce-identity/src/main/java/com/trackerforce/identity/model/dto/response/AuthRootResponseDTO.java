package com.trackerforce.identity.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.Organization;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class AuthRootResponseDTO extends AuthResponseDTO {

	private final AccessDetails access;

	public AuthRootResponseDTO(AuthAccess access, String token, String refreshToken) {
		super(token, refreshToken, true);
		this.access = new AccessDetails(access.getId(), access.getOrganization(), access.getEmail());
	}

    @Override
	public String getId() {
		return access.id();
	}

	record AccessDetails(String id, Organization organization, String email) {

	}

}
