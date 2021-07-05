package com.trackerforce.management.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.type.RenderType;
import com.trackerforce.common.tenant.model.type.TaskType;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.TaskRequest;
import com.trackerforce.management.repository.TaskRepositoryDao;

@Service
public class TaskService extends AbstractBusinessService<Task> {
	
	@Autowired
	private TaskRepositoryDao taskDao;
	
	@Override
	protected Task create(final Task entity) {
		super.create(entity);
		return taskDao.save(entity);
	}

	@Override
	protected void validate(final Task entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.hasText(entity.getDescription(), "'description' must not be empty");
			Assert.hasText(entity.getType(), "'type' must not be empty");
			TaskType.validate(entity.getType());
			
			if (entity.getHelper() != null)
				RenderType.validate(entity.getHelper().getRenderType());
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	public Task createTask(final TaskRequest taskRequest) throws ServiceException {
		var task = taskRequest.getTask();
		this.validate(task);
		
		var helperContentOptional = Optional.ofNullable(taskRequest.getHelper());
		if (helperContentOptional.isPresent()) {
			task.setHelper(taskRequest.getHelper());
		}
		
		return this.create(task);
	}
	
	public Task updateTask(final String id, final Map<String, Object> updates) throws ServiceException {
		var promise = taskDao.getTaskRepository().findById(id);
		
		if (!promise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		
		var task = super.update(promise.get(), updates.get("task"));
		if (updates.containsKey("helper"))
			task.setHelper(super.update(task.getHelper(), updates.get("helper")));
		
		this.validate(task);
		return taskDao.getTaskRepository().save(task);
	}
	
	public Task findByIdProjectedBy(final String id, final String output) {
		final var outputOptional = Optional.ofNullable(output);
		
		if (outputOptional.isPresent())
			return this.taskDao.findByIdProjectedBy(id, Task.class, outputOptional.get().split(","));
		
		var task = this.taskDao.findByIdProjectedBy(id, Task.class);
		if (task == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		
		return task;
	}

}
