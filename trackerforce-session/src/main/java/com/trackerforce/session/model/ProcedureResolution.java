package com.trackerforce.session.model;

import java.util.LinkedList;

import com.trackerforce.common.tenant.model.CommonProcedure;
import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.common.tenant.model.type.TaskType;

public class ProcedureResolution extends CommonProcedure {

	private String resolution;

	private LinkedList<TaskResolution<?>> taskResolution;

	public ProcedureResolution(CommonProcedure input) {
		super.name = input.getName();

		this.taskResolution = new LinkedList<TaskResolution<?>>();
		for (CommonTask task : input.getTasks())
			taskResolution.add(TaskResolution.createTaskResolution(task, TaskType.getType(task.getType())));
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

}
