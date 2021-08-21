package com.trackerforce.session.service;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.model.AbstractBusinessDocument;
import com.trackerforce.common.tenant.service.AbstractTenantService;

public abstract class AbstractSessionService<T extends AbstractBusinessDocument> 
	extends AbstractTenantService<T> {
	
	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;

}
