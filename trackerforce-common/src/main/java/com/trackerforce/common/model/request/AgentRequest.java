package com.trackerforce.common.model.request;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AgentRequest {
	
	private String name;
	
	private String email;
	
	private String password;
	
	@JsonProperty("new_password")
	private String newPassword;
	
	private List<String> roles =  Collections.emptyList();

}
