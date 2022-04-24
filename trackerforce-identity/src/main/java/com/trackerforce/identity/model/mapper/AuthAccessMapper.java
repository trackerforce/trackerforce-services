package com.trackerforce.identity.model.mapper;

import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.dto.request.AccessRequestDTO;

public class AuthAccessMapper {
	
	private AuthAccessMapper() {}
	
	public static AuthAccess getAuthAccess(AccessRequestDTO accessRequestDTO) {
		final var organization = accessRequestDTO.getOrganization();
		organization.setAlias(organization.getName());

		final var authAccess = new AuthAccess();
		authAccess.setEmail(accessRequestDTO.getEmail());
		authAccess.setPassword(accessRequestDTO.getPassword());
		authAccess.setOrganization(organization);

		return authAccess;
	}

}
