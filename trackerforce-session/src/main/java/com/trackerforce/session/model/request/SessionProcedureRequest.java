package com.trackerforce.session.model.request;

import com.trackerforce.session.model.type.SessionProcedureEvent;

public class SessionProcedureRequest {

	private SessionProcedureEvent event;

	public SessionProcedureEvent getEvent() {
		return event;
	}

	public void setEvent(SessionProcedureEvent event) {
		this.event = event;
	}

}
