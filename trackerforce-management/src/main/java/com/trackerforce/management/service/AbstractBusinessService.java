package com.trackerforce.management.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.model.ComponentHelper;
import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.common.tenant.service.AbstractTenantService;
import com.trackerforce.common.tenant.service.exception.InvalidServiceUpdateException;

/**
 * Defines common operations for service components.
 * A reference of AbstractProjectedDao is borrowed from the service to operate generic DB queries.
 * 
 * @author Roger Floriano
 */
public abstract class AbstractBusinessService<T extends AbstractBusinessDocument> 
	extends AbstractTenantService<T> {
	
	public static final String[] ALLOWED_HELPER_UPDATE = { "content", "renderType" };
	
	/**
	 * Name given by request objects
	 */
	protected final String entityName;
	
	protected final AbstractProjectedDao<T> dao;
	
	protected final Class<T> serviceModel;
	
	public AbstractBusinessService(
			final AbstractProjectedDao<T> dao, 
			final Class<T> serviceModel,
			final String entityName) {
		this.dao = dao;
		this.serviceModel = serviceModel;
		this.entityName = entityName;
	}
	
	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;
	
	/**
	 * Generic entity creator
	 * 
	 * @param entity to be created
	 * @param helper Optional helper component
	 * @return Entity saved
	 * @throws ServiceException if validation has errors
	 */
	public T create(final T entity, final ComponentHelper helper) throws ServiceException {
		var helperContentOptional = Optional.ofNullable(helper);
		if (helperContentOptional.isPresent())
			entity.setHelper(helperContentOptional.get());
		
		validate(entity);
		return dao.save(entity);
	}
	
	/**
	 * Generic entity creator
	 * 
	 * @param entity to be created
	 * @param helper Optional helper component
	 * @return Entity saved
	 * @throws ServiceException if validation has errors
	 */
	public T create(final T entity) throws ServiceException {
		return create(entity, null);
	}
	
	/**
	 * Generic entity updater
	 * 
	 * @param promise Db query
	 * @param updates Values to be updated
	 * @param allowedUpdate Values that can be updated
	 * @return Entity updated
	 * @throws ServiceException if validation has errors
	 */
	public T update(
			final Optional<T> promise,
			final Map<String, Object> updates,
			final Map<String, String[]> allowedUpdate) throws ServiceException {
		
		if (!promise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceModel.getSimpleName() + " not found");
		
		var entity = promise.get();
		for (String key : allowedUpdate.keySet()) {
			if (key.equals(entityName)) {
				super.update(entity, updates.get(key), allowedUpdate.get(key));
			} else {
				var field = ReflectionUtils.findField(entity.getClass(), key);	
				
				if (field == null)
					throw new InvalidServiceUpdateException(key);
				try {
					field.setAccessible(true);
					super.update(field.get(entity), updates.get(key), allowedUpdate.get(key));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new InvalidServiceUpdateException(key, e);
				} finally {
					field.setAccessible(false);
				}
			}
		}
		
		validate(entity);
		return dao.save(entity);
	}
	
	/**
	 * {@link AbstractBusinessService#findByIdProjectedBy(String, String)}
	 */
	public T findByIdProjectedBy(
			final String id, 
			final String output) {
		
		final var outputOptional = Optional.ofNullable(output);

		if (outputOptional.isPresent())
			return dao.findByIdProjectedBy(id, serviceModel, outputOptional.get().split(","));

		var procedure = dao.findByIdProjectedBy(id, serviceModel);
		if (procedure == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceModel.getSimpleName() + " not found");

		return procedure;
	}
	
	/**
	 * {@link AbstractBusinessService#findAllProjectedBy(String, String, String, int, int)}
	 */
	public Map<String, Object> findAllProjectedBy(
			final String description,
			final String sortBy,
			final String output,
			final int page, 
			final int size) {
		
		final var criteriaOptional = Optional.ofNullable(description);
		final var outputOptional = Optional.ofNullable(output);
		final var sortOptional = Optional.ofNullable(sortBy);
		
		Criteria criteria = null;
		if (criteriaOptional.isPresent())
			criteria = Criteria.where("description").regex(String.format(".*%s.*", description), "i");
		
		var pageTasks = dao.findByProjectedBy(
				serviceModel, criteria, page, size, 
				outputOptional.isPresent() ? outputOptional.get().split(",") : null,
				sortOptional.isPresent() ? sortOptional.get().split(",") : null);
		
		var tasks = pageTasks.getContent();
		var response = new HashMap<String, Object>();
		response.put("data", tasks);
		response.put("page", pageTasks.getNumber());
		response.put("items", pageTasks.getTotalElements());
		response.put("pages", pageTasks.getTotalPages());

		return response;
	}

}
