package com.trackerforce.management.service;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.service.AbstractTenantService;
import com.trackerforce.management.model.AbstractBusinessDocument;

public abstract class AbstractBusinessService<T extends AbstractBusinessDocument> 
	extends AbstractTenantService<T> {
	
	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;

}
