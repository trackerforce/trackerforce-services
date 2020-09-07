package com.trackerforce.common.tenant.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.trackerforce.common.model.AbstractDocument;
import com.trackerforce.common.tenant.interceptor.TenantInterceptor;

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

}
