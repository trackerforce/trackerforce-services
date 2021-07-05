package com.trackerforce.common.tenant.service.exception;

import com.trackerforce.common.service.exception.ServiceException;

@SuppressWarnings("serial")
public class InvalidServiceUpdateException extends ServiceException {

	public InvalidServiceUpdateException(String update, Class<?> modelClass, Throwable e) {
		super(String.format("Updating '%s' from %s has errors", update, modelClass), e);
	}
	
	public InvalidServiceUpdateException(String update, Class<?> modelClass) {
		super(String.format("Updating '%s' from %s has errors", update, modelClass));
	}

}
