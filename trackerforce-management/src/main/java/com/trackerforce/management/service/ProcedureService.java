package com.trackerforce.management.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.CommonTask;
import com.trackerforce.common.tenant.model.type.RenderType;
import com.trackerforce.management.model.Procedure;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.ProcedureRequest;
import com.trackerforce.management.repository.ProcedureRepositoryDao;
import com.trackerforce.management.repository.TaskRepositoryDao;

@Service
public class ProcedureService extends AbstractBusinessService<Procedure> {

	private static final String[] ALLOWED_PROC_UPDATE = { "name", "description", "helper" };
	private static final String[] ALLOWED_CP_UPDATE = { "agentId" };

	private final ProcedureRepositoryDao procedureDao;

	private final TaskRepositoryDao taskDao;

	public ProcedureService(TaskRepositoryDao taskDao, ProcedureRepositoryDao procedureDao) {
		super(procedureDao, Procedure.class, "procedure");
		this.procedureDao = procedureDao;
		this.taskDao = taskDao;
	}

	@Override
	protected void validate(final Procedure entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.hasText(entity.getDescription(), "'description' must not be empty");
			Assert.hasText(entity.getName(), "'name' must not be empty");

			if (entity.getHelper() != null)
				RenderType.validate(entity.getHelper().getRenderType());
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public Procedure create(final ProcedureRequest procedureRequest) throws ServiceException {
		var procedure = procedureRequest.getProcedure();
		return super.create(procedure, procedureRequest.getHelper());
	}

	public Procedure update(final String id, final Map<String, Object> updates) throws ServiceException {
		var promise = procedureDao.getProcedureRepository().findById(id);
		var allowed = new HashMap<String, String[]>();
		allowed.put(entityName, ALLOWED_PROC_UPDATE);
		allowed.put("hook", ALLOWED_CP_UPDATE);
		allowed.put("helper", ALLOWED_HELPER_UPDATE);

		return super.update(promise, updates, allowed);
	}

	public LinkedList<CommonTask> reorderTask(final String id, int from, int to) {
		final var procedurePromise = procedureDao.getProcedureRepository().findById(id);

		if (!procedurePromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedure not found");

		final var procedure = procedurePromise.get();
		final var task = procedure.getTasks().get(from);
		procedure.getTasks().remove(from);
		procedure.getTasks().add(to, task);

		procedureDao.getProcedureRepository().save(procedure);
		return procedure.getTasks();
	}

	public Task updateTasks(final String id, final String taskId, boolean add) {
		final var procedurePromise = procedureDao.getProcedureRepository().findById(id);
		final var taskPromise = taskDao.getTaskRepository().findById(taskId);

		if (!procedurePromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedure not found");

		if (!taskPromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");

		if (add)
			return addTask(procedurePromise.get(), taskPromise.get());

		return removeTask(procedurePromise.get(), taskPromise.get());
	}

	private Task addTask(final Procedure procedure, final Task task) {
		if (procedure.getTasks().contains(task))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task already added to Procedure");

		procedure.getTasks().push(task);
		procedureDao.getProcedureRepository().save(procedure);
		return task;
	}

	private Task removeTask(final Procedure procedure, final Task task) {
		if (!procedure.getTasks().contains(task))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task does not exist into Procedure");

		procedure.getTasks().remove(task);
		procedureDao.getProcedureRepository().save(procedure);
		return task;
	}

	public void delete(final String id) {
		procedureDao.deleteById(id);
	}

}
