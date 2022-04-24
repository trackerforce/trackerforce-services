package com.trackerforce.identity.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.identity.model.AuthAccess;
import com.trackerforce.identity.model.Organization;

@JsonInclude(Include.NON_NULL)
public class AuthRootResponseDTO extends AuthResponseDTO {

	private AccessDetails access;

	public AuthRootResponseDTO(AuthAccess access, String token, String refreshToken) {
		super(token, refreshToken, true);
		this.access = new AccessDetails(access.getId(), access.getOrganization(), access.getEmail());
	}

	public AccessDetails getAccess() {
		return access;
	}
	
	@Override
	public String getId() {
		return access.getId();
	}

	class AccessDetails {

		private String id;

		private Organization organization;

		private String email;

		public AccessDetails(String id, Organization organization, String email) {
			this.id = id;
			this.organization = organization;
			this.email = email;
		}

		public String getId() {
			return id;
		}

		public Organization getOrganization() {
			return organization;
		}

		public String getEmail() {
			return email;
		}

	}

}
