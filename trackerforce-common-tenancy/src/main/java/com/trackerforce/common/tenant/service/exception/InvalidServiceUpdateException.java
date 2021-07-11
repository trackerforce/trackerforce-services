package com.trackerforce.common.tenant.service.exception;

import com.trackerforce.common.service.exception.ServiceException;

@SuppressWarnings("serial")
public class InvalidServiceUpdateException extends ServiceException {

	public InvalidServiceUpdateException(String update, Throwable e) {
		super(String.format("Updating '%s' is not allowed", update), e);
	}
	
	public InvalidServiceUpdateException(String update) {
		super(String.format("Updating '%s' is not allowed", update));
	}

}
