package com.trackerforce.session.model;

import java.util.LinkedList;

import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.session.model.type.ProcedureStatus;

public class SessionProcedure extends CommonProcedure {

	private String resolution;

	private ProcedureStatus status;

	private LinkedList<SessionTask> taskResolution;

	public SessionProcedure() {
	}

	private SessionProcedure(CommonProcedure input) {
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
	public static SessionProcedure create(CommonProcedure input) {
		var resolution = new SessionProcedure(input);
		for (CommonTask task : input.getTasks())
			resolution.getTaskResolution().add(SessionTask.createTaskResolution(task));

		resolution.setStatus(ProcedureStatus.OPENED);
		return resolution;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public LinkedList<SessionTask> getTaskResolution() {
		if (taskResolution == null)
			this.taskResolution = new LinkedList<SessionTask>();
		return taskResolution;
	}

	public void setTaskResolution(LinkedList<SessionTask> taskResolution) {
		this.taskResolution = taskResolution;
	}

	public ProcedureStatus getStatus() {
		return status;
	}

	public void setStatus(ProcedureStatus status) {
		this.status = status;
	}

}
