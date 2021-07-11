package com.trackerforce.common.tenant.service;

import java.util.Arrays;
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
 * @param <T AbstractDocument>
 */
public abstract class AbstractTenantService<T extends AbstractDocument> {
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * Validates whether there is invalid update attributes
	 * 
	 * @param updates body request
	 * @param allowed attributes
	 * @throws InvalidServiceUpdateException
	 */
	private void validateUpdate(final Map<String, Object> updates, final String[] allowed) 
			throws InvalidServiceUpdateException {
		for (String update : updates.keySet())
			if (!Arrays.stream(allowed).anyMatch(key -> key.equals(update)))
				throw new InvalidServiceUpdateException(update);
	}
	
	/**
	 * Link component with organization owner
	 * 
	 * @param entity Entity to be linked
	 */
	protected void linkOrganization(final T entity) {
		entity.setOrganizationId(request.getAttribute(TenantInterceptor.ORGANIZATION_ID).toString());
	}
	
	/**
	 * Update entity given a map of attributes
	 * 
	 * @param entity to be updated
	 * @param updates declared at the requets body
	 * @return updated entity
	 * @throws InvalidServiceUpdateException if something went wrong while assigning a new value to entity
	 */
	protected <Y> Y update(
			final Y entity, 
			final Map<String, Object> updates,
			final String[] allowed) throws InvalidServiceUpdateException {
		this.validateUpdate(updates, allowed);
		final var ignored = "id";
		
		for (String key : updates.keySet()) {
			var field = ReflectionUtils.findField(entity.getClass(), key);
			
			if (field == null || ignored.equals(key))
				throw new InvalidServiceUpdateException(key);

			try {
				field.setAccessible(true);
				field.set(entity, updates.get(key));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new InvalidServiceUpdateException(key, e);
			} finally {
				field.setAccessible(false);
			}	
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	protected <Y> Y update(final Y entity, Object updates, final String[] allowed) 
			throws InvalidServiceUpdateException {
		var wrapper = (Map<String, Object>) updates;
		return update(entity, wrapper, allowed);
	}

}
