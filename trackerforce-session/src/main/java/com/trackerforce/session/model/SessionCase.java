package com.trackerforce.session.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cases")
public class SessionCase extends AbstractSessionDocument {

	private long protocol;

	private List<ProcedureResolution> procedures;

	public long getProtocol() {
		return protocol;
	}

	public void setProtocol(long protocol) {
		this.protocol = protocol;
	}

	public List<ProcedureResolution> getProcedures() {
		if (procedures == null)
			procedures = new ArrayList<ProcedureResolution>();
		return procedures;
	}

	public void setProcedures(List<ProcedureResolution> procedures) {
		this.procedures = procedures;
	}

}
