package com.trackerforce.management.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.type.RenderType;
import com.trackerforce.management.model.Procedure;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.ProcedureRequest;
import com.trackerforce.management.repository.ProcedureRepositoryDao;
import com.trackerforce.management.repository.TaskRepositoryDao;

@Service
public class ProcedureService extends AbstractBusinessService<Procedure> {

	@Autowired
	private ProcedureRepositoryDao procedureDao;
	
	@Autowired
	private TaskRepositoryDao taskDao;

	@Override
	protected Procedure create(final Procedure entity) {
		super.create(entity);
		return procedureDao.getProcedureRepository().save(entity);
	}

	@Override
	protected void validate(final Procedure entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.notNull(entity.getCheckpoint(), "Procedure checkpoint must not be null");
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
		this.validate(procedure);

		var helperContentOptional = Optional.ofNullable(procedureRequest.getHelper());
		if (helperContentOptional.isPresent())
			procedure.setHelper(procedureRequest.getHelper());

		return this.create(procedure);
	}
	
	public Task updateTasks(final String id, final String taskId, boolean add) {
		final var procedurePromise = procedureDao.getProcedureRepository().findById(id);
		final var taskPromise = taskDao.getTaskRepository().findById(taskId);
		
		if (!procedurePromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
					"Procedure not found");
		
		if (!taskPromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
					"Task not found");
		
		if (add)
			return addTask(procedurePromise.get(), taskPromise.get());
		
		return removeTask(procedurePromise.get(), taskPromise.get());
	}
	
	private Task addTask(final Procedure procedure, final Task task) {
		if (procedure.getTasks().contains(task))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Task already added to Procedure");
		
		procedure.getTasks().add(task);
		procedureDao.getProcedureRepository().save(procedure);
		return task;
	}
	
	private Task removeTask(final Procedure procedure, final Task task) {
		if (!procedure.getTasks().contains(task))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Task does not exist into Procedure");
		
		procedure.getTasks().remove(task);
		procedureDao.getProcedureRepository().save(procedure);
		return task;
	}

	public void delete(final String id) {
		procedureDao.deleteById(id);
	}

}
