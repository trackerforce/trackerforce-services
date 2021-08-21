package com.trackerforce.session.model.type;

import com.trackerforce.session.service.exception.InvalidStatusException;

public enum ProcedureStatus {
	
	OPENED,
	CLOSED,
	CANCELED;
	
	public static void validate(String type) throws InvalidStatusException {
		try {
			ProcedureStatus.valueOf(type);
		} catch (Exception e) {
			throw new InvalidStatusException(type, e);
		}
	}
}
