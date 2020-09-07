package com.trackerforce.session.service;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.service.AbstractTenantService;
import com.trackerforce.session.model.AbstractSessionDocument;

public abstract class AbstractSessionService<T extends AbstractSessionDocument> 
	extends AbstractTenantService<T> {
	
	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;

}
