package com.trackerforce.management.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
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
 * Defines common operations for service components. A reference of
 * AbstractProjectedDao is borrowed from the service to operate generic DB
 * queries.
 * 
 * @author Roger Floriano
 */
public abstract class AbstractBusinessService<T extends AbstractBusinessDocument, R extends MongoRepository<T, String>> 
	extends AbstractTenantService<T, R> {

	public static final String[] ALLOWED_HELPER_UPDATE = { "content", "renderType" };

	/**
	 * Name given by request objects
	 */
	protected final String entityName;

	public AbstractBusinessService(final AbstractProjectedDao<T, R> dao, final Class<T> serviceModel,
			final String entityName) {
		super(dao, serviceModel);
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
	 * @param promise       Db query
	 * @param updates       Values to be updated
	 * @param allowedUpdate Values that can be updated
	 * @return Entity updated
	 * @throws ServiceException if validation has errors
	 */
	public T update(final Optional<T> promise, final Map<String, Object> updates,
			final Map<String, String[]> allowedUpdate) throws ServiceException {

		if (!promise.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceModel.getSimpleName() + " not found");

		var entity = promise.get();
		for (String key : allowedUpdate.keySet()) {
			if (key.equals(entityName)) {
				super.update(entity, updates.get(key), allowedUpdate.get(key));
			} else if (updates.containsKey(key)) {
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

}
