package com.trackerforce.common.service.exception;

@SuppressWarnings("serial")
public class ServiceException extends Exception {

	public ServiceException(final String error, final Throwable exception) {
		super(error, exception);
	}

}
