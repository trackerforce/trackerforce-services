package com.trackerforce.identity.service;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.identity.model.AbstractIdentityDocument;

public abstract class AbstractIdentityService<T extends AbstractIdentityDocument> {
	
	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;

}
