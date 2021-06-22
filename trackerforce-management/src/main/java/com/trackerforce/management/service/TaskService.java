package com.trackerforce.management.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.type.TaskType;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.TaskRequest;
import com.trackerforce.management.repository.TaskRepositoryDao;

@Service
public class TaskService extends AbstractBusinessService<Task> {
	
	@Autowired
	private TaskRepositoryDao taskDao;
	
	public Task createTask(final TaskRequest taskRequest) throws ServiceException {
		var task = taskRequest.getTask();
		this.validate(task);
		
		var helperContentOptional = Optional.ofNullable(taskRequest.getHelper());
		if (helperContentOptional.isPresent()) {
			task.setHelper(taskRequest.getHelper());
		}
		
		return this.create(task);
	}
	
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
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	public Task findByIdProjectedBy(final String id, final String output) {
		final Optional<String> outputOptional = Optional.ofNullable(output);
		
		if (outputOptional.isPresent())
			return this.taskDao.findByIdProjectedBy(id, Task.class, outputOptional.get().split(","));
		
		var task = this.taskDao.findByIdProjectedBy(id, Task.class);
		if (task == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		
		return task;
	}

}
