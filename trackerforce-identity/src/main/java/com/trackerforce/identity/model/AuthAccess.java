package com.trackerforce.identity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trackerforce.common.model.type.JwtKeys;
import com.trackerforce.common.model.type.ServicesRole;
import com.trackerforce.identity.model.dto.request.JwtRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "auth-access")
@Data
@NoArgsConstructor
public class AuthAccess extends AbstractIdentityDocument {
	
	private String email;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String refreshToken;
	
	@JsonIgnore
	private String tokenHash;
	
	@JsonIgnore
	private boolean root = true;
	
	public AuthAccess(JwtRequestDTO authRequest, Organization organization) {
		this.email = authRequest.getEmail();
		this.password = authRequest.getPassword();
		setOrganization(organization);
	}
	
	/**
	 * Agents and Session users
	 */
	public AuthAccess(String username, String orgAlias) {
		this.email = username;
		setOrganizationByAlias(orgAlias);
		setRoot(false);
	}
	
	public boolean hasValidOrganization() {
		if (super.organization == null)
			return false;
		
		return StringUtils.hasText(super.organization.getName());
	}
	
	@JsonIgnore
	public Map<String, Object> getDefaultClaims() {
		var claims = new HashMap<String, Object>();
		claims.put(JwtKeys.ROLES.toString(), List.of(ServicesRole.ROOT.name()));
		return claims;
	}

}
