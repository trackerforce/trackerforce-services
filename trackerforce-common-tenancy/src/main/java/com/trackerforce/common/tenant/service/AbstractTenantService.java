package com.trackerforce.common.tenant.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.trackerforce.common.model.AbstractDocument;
import com.trackerforce.common.tenant.interceptor.TenantInterceptor;
import com.trackerforce.common.tenant.service.exception.InvalidServiceUpdateException;

/**
 * Defines document organization owner
 * 
 * @author Roger Floriano
 * @since 2020-07-22
 *
 * @param <T>
 */
public abstract class AbstractTenantService<T extends AbstractDocument> {
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * Link component with organization owner
	 * 
	 * @param entity Entity to be linked
	 * @return Created component
	 */
	protected T create(final T entity) {
		entity.setOrganizationId(request.getAttribute(TenantInterceptor.ORGANIZATION_ID).toString());
		return entity;
	}
	
	/**
	 * Update entity given a map of attributes
	 * 
	 * @param entity to be updated
	 * @param updates declared at the requets body
	 * @return updated entity
	 * @throws InvalidServiceUpdateException if something went wrong while assigning a new value to entity
	 */
	protected <Y> Y update(final Y entity, Map<String, Object> updates) throws InvalidServiceUpdateException {
		final var ignored = "id";
		
		for (String key : updates.keySet()) {
			var field = ReflectionUtils.findField(entity.getClass(), key);
			
			if (field == null || ignored.equals(key))
				throw new InvalidServiceUpdateException(key, entity.getClass());

			try {
				field.setAccessible(true);
				field.set(entity, updates.get(key));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new InvalidServiceUpdateException(key, entity.getClass(), e);
			} finally {
				field.setAccessible(false);
			}	
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	protected <Y> Y update(final Y entity, Object updates) throws InvalidServiceUpdateException {
		var wrapper = (Map<String, Object>) updates;
		return update(entity, wrapper);
	}

}
