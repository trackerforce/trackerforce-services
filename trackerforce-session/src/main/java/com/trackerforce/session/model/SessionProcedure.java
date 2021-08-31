package com.trackerforce.session.model;

import java.util.LinkedList;

import com.trackerforce.common.model.exception.BusinessException;
import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.common.tenant.model.exception.InvalidStatusChangeException;
import com.trackerforce.session.model.type.ProcedureStatus;

public class SessionProcedure extends CommonProcedure<SessionTask> {

	private String resolution;

	private ProcedureStatus status = ProcedureStatus.OPENED;
	
	protected LinkedList<SessionTask> tasks;

	public SessionProcedure() {
	}

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

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public ProcedureStatus getStatus() {
		return status;
	}

	@Override
	public LinkedList<SessionTask> getTasks() {
		if (tasks == null)
			tasks = new LinkedList<SessionTask>();
		return tasks;
	}

}
