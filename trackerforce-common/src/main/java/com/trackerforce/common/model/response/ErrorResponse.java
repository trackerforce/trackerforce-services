package com.trackerforce.common.model.response;

public class ErrorResponse {
	
	private String error;
	
	public ErrorResponse(final String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

}
