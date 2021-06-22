package com.trackerforce.common.tenant.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.tenant.model.type.TaskType;

@JsonInclude(Include.NON_NULL)
public abstract class AbstractTask extends AbstractBusinessDocument {
	
	/**
	 * {@link TaskType}
	 */
	private String type;
	
	private List<TaskOption> options;

	public AbstractTask(String type, String description, List<TaskOption> options) {
		setDescription(description);
		this.type = type;
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TaskOption> getOptions() {
		return options;
	}

	public void setOptions(List<TaskOption> options) {
		this.options = options;
	}

}
