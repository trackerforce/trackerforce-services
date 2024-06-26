package com.trackerforce.session.model;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trackerforce.common.model.exception.BusinessException;
import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.common.tenant.model.exception.InvalidStatusChangeException;
import com.trackerforce.session.model.type.ProcedureStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SessionProcedure extends CommonProcedure<SessionTask> {
	
	@JsonProperty("context_id")
	private String contextId;

	private String resolution;

	private ProcedureStatus status = ProcedureStatus.OPENED;

	private LinkedList<SessionTask> tasks;

	private SessionProcedure(CommonProcedure<?> input) {
		super.name = input.getName();
		super.setId(input.getId());
		super.setDescription(input.getDescription());
		super.setHelper(input.getHelper());
	}

	/**
	 * Creates a procedure from the Management template
	 * 
	 * @param input Procedure from the management template
	 * @return New Session Case procedure
	 */
	public static SessionProcedure create(CommonProcedure<?> input) {
		var resolution = new SessionProcedure(input);
		for (CommonTask task : input.getTasks())
			resolution.getTasks().add(SessionTask.createTaskResolution(task));

		return resolution;
	}

	public SessionProcedure changeStatus(ProcedureStatus newStatus) throws BusinessException {
		if (!status.canChange(newStatus.name()))
			throw new InvalidStatusChangeException(getStatus().name(), status.name());

		this.status = newStatus;
		return this;
	}

	public boolean canSubmit() {
		for (SessionTask task : getTasks())
			if (task.getResponse() == null)
				return false;
		
		return true;
	}

	@Override
	public LinkedList<SessionTask> getTasks() {
		if (tasks == null)
			tasks = new LinkedList<>();
		return tasks;
	}
	
	@JsonIgnore
	public boolean isClosingProcedure() {
		return getTasks().isEmpty();
	}

}
