package com.trackerforce.session.model;

import java.util.LinkedList;

import com.trackerforce.common.tenant.model.CommonTemplate;

public class SessionTemplate extends CommonTemplate<SessionProcedure> {
	
	private LinkedList<SessionProcedure> procedures;
	
	@Override
	public LinkedList<SessionProcedure> getProcedures() {
		if (procedures == null)
			procedures = new LinkedList<SessionProcedure>();
		return procedures;
	}

}
