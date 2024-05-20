package com.trackerforce.identity.model.dto.request;

import com.trackerforce.identity.model.Organization;
import lombok.Data;

@Data
public class AccessRequestDTO {

	private String email;

	private String password;
	
	private Organization organization;

}
