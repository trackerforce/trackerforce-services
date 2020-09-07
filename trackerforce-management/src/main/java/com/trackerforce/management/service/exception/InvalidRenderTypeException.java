package com.trackerforce.management.service.exception;

import java.util.Arrays;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.type.RenderType;

@SuppressWarnings("serial")
public class InvalidRenderTypeException extends ServiceException {

	public InvalidRenderTypeException(String invalidRenderType, Throwable e) {
		super(String.format(
				"Render type '%s' does not exist. Try one of these: %s", 
				invalidRenderType, Arrays.toString(RenderType.values())), e);
	}

}
