package com.trackerforce.session.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackerforce.common.model.AbstractDocument;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.common.tenant.service.AbstractTenantService;

public abstract class AbstractSessionService<T extends AbstractDocument, R extends MongoRepository<T, String>> 
	extends AbstractTenantService<T, R> {

	protected AbstractSessionService(AbstractProjectedDao<T, R> dao, Class<T> serviceModel) {
		super(dao, serviceModel);
	}

	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;

}
