package com.trackerforce.session.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTemplate;
import com.trackerforce.common.tenant.model.ComponentHelper;

@Document(collection = "cases")
public class SessionCase extends AbstractSessionDocument {

	private long protocol;

	private String contextId;

	private String context;

	private String description;

	private ComponentHelper helper;

	private List<SessionProcedure> procedures;

	private SessionCase(CommonTemplate input) {
		this.contextId = input.getId();
		this.context = input.getName();
		this.description = input.getDescription();
		this.helper = input.getHelper();
	}

	public static SessionCase create(CommonTemplate input) {
		var sessionCase = new SessionCase(input);

		sessionCase.setProtocol(System.currentTimeMillis() / 1000);
		for (CommonProcedure proc : input.getProcedures())
			sessionCase.getProcedures().add(SessionProcedure.create(proc));

		return sessionCase;
	}

	public long getProtocol() {
		return protocol;
	}

	public void setProtocol(long protocol) {
		this.protocol = protocol;
	}

	public List<SessionProcedure> getProcedures() {
		if (procedures == null)
			procedures = new ArrayList<SessionProcedure>();
		return procedures;
	}

	public void setProcedures(List<SessionProcedure> procedures) {
		this.procedures = procedures;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ComponentHelper getHelper() {
		return helper;
	}

	public void setHelper(ComponentHelper helper) {
		this.helper = helper;
	}

}
