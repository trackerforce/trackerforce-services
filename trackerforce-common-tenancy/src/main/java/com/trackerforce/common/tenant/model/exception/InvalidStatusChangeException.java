package com.trackerforce.common.tenant.model.exception;

import com.trackerforce.common.model.exception.BusinessException;

@SuppressWarnings("serial")
public class InvalidStatusChangeException extends BusinessException {

	public InvalidStatusChangeException(String from, String to) {
		super(String.format("Cannot change status from '%s' to '%s'", from, to));
	}

}
