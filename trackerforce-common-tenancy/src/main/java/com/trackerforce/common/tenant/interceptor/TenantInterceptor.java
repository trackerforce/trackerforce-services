package com.trackerforce.common.tenant.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.trackerforce.common.tenant.service.IdentityService;

/**
 * Interceptor responsible for defining business ownership
 * 
 * @author Roger Floriano
 * @since 2020-07-22
 *
 */
public class TenantInterceptor implements HandlerInterceptor {
	
	public static final String TENANT_ID = "TenantId";
	
	public static final String TENANT_HEADER = "X-Tenant";
	
	public static final String ORGANIZATION_ID = "OrganizationId";
	
	private final String DEDICATED_IDENTIFIER = "#";
	
	private final String TENANT_SHARED = "shared";
	
	private final IdentityService identityService;
	
	public TenantInterceptor(IdentityService identityService) {
		this.identityService = identityService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		final Optional<String> tenant = Optional.ofNullable(request.getHeader(TENANT_HEADER));
		
		if (tenant.isPresent() && StringUtils.hasText(tenant.get())) {
			request.setAttribute(ORGANIZATION_ID, tenant.get().replace(DEDICATED_IDENTIFIER, Strings.EMPTY));
			if (tenant.get().contains(DEDICATED_IDENTIFIER)) {
				request.setAttribute(TENANT_ID, tenant.get().replace(DEDICATED_IDENTIFIER, Strings.EMPTY));
			} else {
				request.setAttribute(TENANT_ID, TENANT_SHARED);
			}
			
			if (!identityService.validateIdentity(request)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}

		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}
