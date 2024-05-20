package com.trackerforce.management.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.type.RenderType;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.TaskRequest;
import com.trackerforce.management.repository.TaskRepository;
import com.trackerforce.management.repository.TaskRepositoryDao;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService extends AbstractBusinessService<Task, TaskRepository> {

	private static final String[] ALLOWED_TASK_UPDATE = { "description", "type", "options" };

	private final TaskRepositoryDao taskDao;

	public TaskService(TaskRepositoryDao taskDao) {
		super(taskDao, Task.class, "task");
		this.taskDao = taskDao;
	}

	@Override
	protected void validate(final Task entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.notNull(entity.getType(), "'type' must not be empty");
			Assert.hasText(entity.getDescription(), "'description' must not be empty");
			
			if (entity.isLearn())
				Assert.isTrue(entity.getType().isLearnable(), "Type cannot be trained");
			
			if (entity.getHelper() != null)
				RenderType.validate(entity.getHelper().getRenderType());
			
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public Task create(final TaskRequest taskRequest) {
		try {
			var task = taskRequest.getTask();
			return super.create(task, taskRequest.getHelper());
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	public Task update(final String id, final Map<String, Object> updates) {
		try {
			var promise = taskDao.getRepository().findById(id);
			var allowed = new HashMap<String, String[]>();
			allowed.put(entityName, ALLOWED_TASK_UPDATE);
			allowed.put("helper", ALLOWED_HELPER_UPDATE);

			return super.update(promise, updates, allowed);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	public void delete(final String id) {
		taskDao.deleteById(id);
	}

}
