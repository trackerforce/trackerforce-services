package com.trackerforce.session.model.request;

import com.trackerforce.session.model.type.SessionEvent;

public class SessionCaseRequest {

	private SessionEvent event;

	private String template;

	private String procedure;

	public SessionEvent getEvent() {
		return event;
	}

	public void setEvent(SessionEvent event) {
		this.event = event;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

}
