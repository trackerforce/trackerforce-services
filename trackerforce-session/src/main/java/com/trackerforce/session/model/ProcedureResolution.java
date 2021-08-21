package com.trackerforce.session.model;

import java.util.LinkedList;

import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.common.tenant.model.type.TaskType;
import com.trackerforce.session.model.type.ProcedureStatus;

public class ProcedureResolution extends CommonProcedure {

	private String resolution;

	private ProcedureStatus status;

	private LinkedList<TaskResolution<?>> taskResolution;

	public ProcedureResolution(CommonProcedure input) {
		super.name = input.getName();
		super.setDescription(input.getDescription());
		super.setHelper(input.getHelper());

		this.taskResolution = new LinkedList<TaskResolution<?>>();
		for (CommonTask task : input.getTasks())
			taskResolution.add(TaskResolution.createTaskResolution(task, TaskType.getType(task.getType())));

		status = ProcedureStatus.OPENED;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public LinkedList<TaskResolution<?>> getTaskResolution() {
		return taskResolution;
	}

	public void setTaskResolution(LinkedList<TaskResolution<?>> taskResolution) {
		this.taskResolution = taskResolution;
	}

	public ProcedureStatus getStatus() {
		return status;
	}

	public void setStatus(ProcedureStatus status) {
		this.status = status;
	}

}
