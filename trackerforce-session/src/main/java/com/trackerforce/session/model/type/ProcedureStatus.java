package com.trackerforce.session.model.type;

import java.util.Arrays;

import com.trackerforce.session.service.exception.InvalidStatusException;

public enum ProcedureStatus {

	/**
	 * A new procedure is created and visible to be filled out
	 */
	OPENED("SUBMITTED", "CANCELED"),

	/**
	 * All input were filled out and the procedure is ready to be processed
	 */
	SUBMITTED("OPENED", "RESOLVED", "CANCELED"),

	/**
	 * Procedure processed
	 */
	RESOLVED,

	/**
	 * Procedure removed from the case
	 */
	CANCELED;

	private final String[] canChangeTo;

	private ProcedureStatus(String... canChangeTo) {
		this.canChangeTo = canChangeTo;
	}

	public boolean canChange(String to) {
		return Arrays.asList(canChangeTo).contains(to);
	}

	public static void validate(String type) throws InvalidStatusException {
		try {
			ProcedureStatus.valueOf(type);
		} catch (Exception e) {
			throw new InvalidStatusException(type, e);
		}
	}
}
