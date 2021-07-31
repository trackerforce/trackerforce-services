package com.trackerforce.common.model.type;

public enum JwtKeys {
	
	ROLES("roles"),
	ACCESS("access"),
	TOKEN("token"),
	REFRESH_TOKEN("refreshToken");
	
	private String name;

	private JwtKeys(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
