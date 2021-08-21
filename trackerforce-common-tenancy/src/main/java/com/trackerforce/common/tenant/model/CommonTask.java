package com.trackerforce.common.tenant.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.tenant.model.type.TaskType;

@JsonInclude(Include.NON_NULL)
public class CommonTask extends AbstractBusinessDocument {

	/**
	 * {@link TaskType}
	 */
	protected String type;

	protected boolean learn;

	protected boolean hidden;

	protected List<TaskOption> options;

	public CommonTask() {
	}

	public CommonTask(String type, String description, List<TaskOption> options) {
		setDescription(description);
		this.type = type;
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public List<TaskOption> getOptions() {
		return options;
	}

	public boolean isLearn() {
		return learn;
	}

	public boolean isHidden() {
		return hidden;
	}

}
