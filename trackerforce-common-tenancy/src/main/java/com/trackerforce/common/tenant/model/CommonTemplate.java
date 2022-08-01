package com.trackerforce.common.tenant.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
public abstract class CommonTemplate<T extends CommonProcedure<? extends CommonTask>> extends AbstractBusinessDocument {

	private String name;

	public abstract List<T> getProcedures();

}
