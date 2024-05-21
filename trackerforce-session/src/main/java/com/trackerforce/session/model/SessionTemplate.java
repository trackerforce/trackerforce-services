package com.trackerforce.session.model;

import java.util.LinkedList;

import com.trackerforce.common.tenant.model.CommonTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SessionTemplate extends CommonTemplate<SessionProcedure> {
	
	private LinkedList<SessionProcedure> procedures;
	
	@Override
	public LinkedList<SessionProcedure> getProcedures() {
		if (procedures == null)
			procedures = new LinkedList<>();
		return procedures;
	}

}
