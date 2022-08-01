package com.trackerforce.common.model.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
	
	private String error;
	
	public ErrorResponse(final String error) {
		this.error = error;
	}

}
