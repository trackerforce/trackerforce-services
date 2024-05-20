package com.trackerforce.management.model;

import java.util.LinkedList;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.trackerforce.common.tenant.model.CommonTemplate;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "templates")
@Data
public class Template extends CommonTemplate<Procedure> {
	
	@DBRef
	private LinkedList<Procedure> procedures;
	
	@Override
	public LinkedList<Procedure> getProcedures() {
		if (procedures == null)
			procedures = new LinkedList<>();
		return procedures;
	}

}
