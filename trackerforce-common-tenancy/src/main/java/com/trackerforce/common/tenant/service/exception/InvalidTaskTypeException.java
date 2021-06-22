package com.trackerforce.common.tenant.service.exception;

import java.util.Arrays;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.type.TaskType;

@SuppressWarnings("serial")
public class InvalidTaskTypeException extends ServiceException {

	public InvalidTaskTypeException(String invalidTaskType, Throwable e) {
		super(String.format(
				"Task '%s' does not exist. Try one of these: %s", 
				invalidTaskType, Arrays.toString(TaskType.values())), e);
	}

}
