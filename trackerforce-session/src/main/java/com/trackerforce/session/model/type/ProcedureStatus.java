package com.trackerforce.session.model.type;

import com.trackerforce.session.service.exception.InvalidStatusException;

public enum ProcedureStatus {

	/**
	 * A new procedure is created and visible to be filled out
	 */
	OPENED,

	/**
	 * All input were filled out and the procedure is ready to be processed
	 */
	SUBMITTED,

	/**
	 * Procedure processed
	 */
	RESOLVED,

	/**
	 * Procedure removed from the case
	 */
	CANCELED;

	public static void validate(String type) throws InvalidStatusException {
		try {
			ProcedureStatus.valueOf(type);
		} catch (Exception e) {
			throw new InvalidStatusException(type, e);
		}
	}
}
