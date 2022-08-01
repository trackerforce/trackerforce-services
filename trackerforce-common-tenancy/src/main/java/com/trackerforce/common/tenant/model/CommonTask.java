package com.trackerforce.common.tenant.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.trackerforce.common.tenant.model.type.TaskType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CommonTask extends AbstractBusinessDocument {

	/**
	 * {@link TaskType}
	 */
	protected TaskType type;

	protected boolean learn;

	protected boolean hidden;

	protected List<TaskOption> options;

	public CommonTask(TaskType type, String description, List<TaskOption> options) {
		setDescription(description);
		this.type = type;
		this.options = options;
	}

}
