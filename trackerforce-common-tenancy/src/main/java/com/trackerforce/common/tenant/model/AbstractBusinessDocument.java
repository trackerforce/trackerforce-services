package com.trackerforce.common.tenant.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.model.AbstractDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public abstract class AbstractBusinessDocument extends AbstractDocument {
	
	private String description;
	
	private ComponentHelper helper;

}
