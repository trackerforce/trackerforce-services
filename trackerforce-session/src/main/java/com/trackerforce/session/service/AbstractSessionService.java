package com.trackerforce.session.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Criteria;

import com.trackerforce.common.model.AbstractDocument;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.common.tenant.service.AbstractTenantService;

public abstract class AbstractSessionService<T extends AbstractDocument> 
	extends AbstractTenantService<T> {
	
	protected final AbstractProjectedDao<T> dao;
	
	protected final Class<T> serviceModel;
	
	public AbstractSessionService(AbstractProjectedDao<T> dao, Class<T> serviceModel) {
		this.dao = dao;
		this.serviceModel = serviceModel;
	}

	/**
	 * Validate populated attributes for a given entity
	 * 
	 * @param entity Entity to be validated
	 * @throws ServiceException if invalid values has been assigned to the entity
	 */
	protected abstract void validate(final T entity) throws ServiceException;
	
	public Map<String, Object> findAllProjectedBy(
			final Map<String, Object> query,
			final String sortBy,
			final String output,
			final int page, 
			final int size) {
		
		final var criteriaOptional = Optional.ofNullable(query);
		final var outputOptional = Optional.ofNullable(output);
		final var sortOptional = Optional.ofNullable(sortBy);
		
		Criteria criteria = null;
		if (criteriaOptional.isPresent()) {
			for (String attr : query.keySet()) {
				if (query.get(attr) == null)
					continue;
				
				var val = String.format(".*%s.*", query.get(attr));
				if (criteria == null)
					criteria = Criteria.where(attr).regex(val, "i");
				else
					criteria = criteria.and(attr).regex(val, "i");
			}
		}
		
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
