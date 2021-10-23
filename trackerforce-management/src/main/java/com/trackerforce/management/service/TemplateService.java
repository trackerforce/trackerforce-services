package com.trackerforce.management.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.type.RenderType;
import com.trackerforce.management.model.Procedure;
import com.trackerforce.management.model.Template;
import com.trackerforce.management.model.request.TemplateRequest;
import com.trackerforce.management.repository.ProcedureRepositoryDao;
import com.trackerforce.management.repository.TemplateRepository;
import com.trackerforce.management.repository.TemplateRepositoryDao;

@Service
public class TemplateService extends AbstractBusinessService<Template, TemplateRepository> {
	
	private static final String[] ALLOWED_TEMPLATE_UPDATE = { "name", "description", "helper" };

	private TemplateRepositoryDao templateDao;
	
	private ProcedureRepositoryDao procedureDao;
	
	public TemplateService(
			TemplateRepositoryDao templateDao,
			ProcedureRepositoryDao procedureDao) {
		super(templateDao, Template.class, "template");
		this.templateDao = templateDao;
		this.procedureDao = procedureDao;
	}

	@Override
	protected void validate(final Template entity) throws ServiceException {
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

	public Template create(final TemplateRequest templateRequest) throws ServiceException {
		var template = templateRequest.getTemplate();
		return super.create(template, templateRequest.getHelper());
	}
	
	public Template update(final String id, final Map<String, Object> updates) 
			throws ServiceException {
		var promise = templateDao.getRepository().findById(id);
		var allowed = new HashMap<String, String[]>();
		allowed.put(entityName, ALLOWED_TEMPLATE_UPDATE);
		allowed.put("helper", ALLOWED_HELPER_UPDATE);
		
		return super.update(promise, updates, allowed);
	}
	
	public LinkedList<Procedure> reorderProcedure(final String id, int from, int to) {
		final var templatePromise = templateDao.getRepository().findById(id);
		
		if (!templatePromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found");
		
		final var procedure = templatePromise.get();
		final var task = procedure.getProcedures().get(from);
		procedure.getProcedures().remove(from);
		procedure.getProcedures().add(to, task);
		
		templateDao.getRepository().save(procedure);
		return procedure.getProcedures();
	}
	
	public Procedure updateProcedures(final String id, final String taskId, boolean add) {
		final var templatePromise = templateDao.getRepository().findById(id);
		final var procedurePromise = procedureDao.getRepository().findById(taskId);
		
		if (!templatePromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found");
		
		if (!procedurePromise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedure not found");
		
		if (add)
			return addProcedure(templatePromise.get(), procedurePromise.get());
		
		return removeProcedure(templatePromise.get(), procedurePromise.get());
	}
	
	private Procedure addProcedure(final Template template, final Procedure procedure) {
		if (template.getProcedures().contains(procedure))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Procedure already added to Template");
		
		template.getProcedures().push(procedure);
		templateDao.getRepository().save(template);
		return procedure;
	}
	
	private Procedure removeProcedure(final Template template, final Procedure procedure) {
		if (!template.getProcedures().contains(procedure))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Procedure does not exist into Template");
		
		template.getProcedures().remove(procedure);
		templateDao.getRepository().save(template);
		return procedure;
	}

	public void delete(final String id) {
		templateDao.deleteById(id);
	}
	
}
