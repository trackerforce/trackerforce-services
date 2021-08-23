package com.trackerforce.common.model.exception;

@SuppressWarnings("serial")
public class BusinessException extends Exception {

	public BusinessException(final String error, final Throwable exception) {
		super(error, exception);
	}
	
	public BusinessException(final String error) {
		super(error);
	}

}
