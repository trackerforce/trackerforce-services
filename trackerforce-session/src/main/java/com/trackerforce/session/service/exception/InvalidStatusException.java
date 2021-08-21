package com.trackerforce.session.service.exception;

import com.trackerforce.common.service.exception.ServiceException;

@SuppressWarnings("serial")
public class InvalidStatusException extends ServiceException {

	public InvalidStatusException(String invalidStatus, Throwable e) {
		super(String.format("Status '%s' does not exist", invalidStatus), e);
	}

}
