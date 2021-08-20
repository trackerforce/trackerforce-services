package com.trackerforce.session.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.AbstractProcedure;

@Document(collection = "cases")
public class SessionCase extends AbstractSessionDocument {

	private String protocol;

	private List<AbstractProcedure> procedures;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public List<AbstractProcedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(List<AbstractProcedure> procedures) {
		this.procedures = procedures;
	}

}
