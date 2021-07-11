package com.trackerforce.management.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
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
	
	private static final String[] ALLOWED_TASK_UPDATE = { "description", "type", "options" };
	private static final String[] ALLOWED_HELPER_UPDATE = { "content", "renderType" };

	@Autowired
	private TaskRepositoryDao taskDao;

	@Override
	protected Task create(final Task entity) {
		super.create(entity);
		return taskDao.getTaskRepository().save(entity);
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

	public Task create(final TaskRequest taskRequest) throws ServiceException {
		var task = taskRequest.getTask();
		this.validate(task);

		var helperContentOptional = Optional.ofNullable(taskRequest.getHelper());
		if (helperContentOptional.isPresent())
			task.setHelper(taskRequest.getHelper());

		return this.create(task);
	}

	public Task update(final String id, final Map<String, Object> updates) 
			throws ServiceException {

		var promise = taskDao.getTaskRepository().findById(id);

		if (!promise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");

		var task = super.update(promise.get(), updates.get("task"), ALLOWED_TASK_UPDATE);
		
		if (updates.containsKey("helper"))
			task.setHelper(super.update(task.getHelper(), 
					updates.get("helper"), ALLOWED_HELPER_UPDATE));

		this.validate(task);
		return taskDao.getTaskRepository().save(task);
	}

	public void delete(final String id) {
		taskDao.deleteById(id);
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

	public Map<String, Object> findAllProjectedBy(
			final String descrition,
			final String sortBy,
			final String output,
			final int page, 
			final int size) {
		
		final var criteriaOptional = Optional.ofNullable(descrition);
		final var outputOptional = Optional.ofNullable(output);
		final var sortOptional = Optional.ofNullable(sortBy);
		
		Criteria criteria = null;
		if (criteriaOptional.isPresent())
			criteria = Criteria.where("description").regex(String.format(".*%s.*", descrition), "i");
		
		var pageTasks = this.taskDao.findByProjectedBy(
				Task.class, criteria, page, size, 
				outputOptional.isPresent() ? outputOptional.get().split(",") : null,
				sortOptional.isPresent() ? sortOptional.get().split(",") : null);
		
		var tasks = pageTasks.getContent();
		var response = new HashMap<String, Object>();
		response.put("tasks", tasks);
		response.put("page", pageTasks.getNumber());
		response.put("items", pageTasks.getTotalElements());
		response.put("pages", pageTasks.getTotalPages());

		return response;
	}

}
