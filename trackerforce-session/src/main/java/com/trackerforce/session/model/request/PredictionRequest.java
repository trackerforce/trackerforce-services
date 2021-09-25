package com.trackerforce.session.model.request;

import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.SessionProcedure;

public class PredictionRequest {

	private String contextId;

	private String context;

	private SessionProcedure procedure;

	public PredictionRequest(SessionCase sessionCase, SessionProcedure sessionProcedure) {
		this.context = sessionCase.getContext();
		this.contextId = sessionCase.getContextId();
		this.procedure = sessionProcedure;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public SessionProcedure getProcedure() {
		return procedure;
	}

	public void setProcedure(SessionProcedure procedure) {
		this.procedure = procedure;
	}

}
