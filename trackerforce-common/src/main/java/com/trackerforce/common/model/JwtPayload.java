package com.trackerforce.common.model;

import java.util.List;

import lombok.Data;

@Data
public class JwtPayload {

	private String sub;

	private List<String> aud;

	private List<String> roles;

}
