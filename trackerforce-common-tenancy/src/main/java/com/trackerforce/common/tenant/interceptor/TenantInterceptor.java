package com.trackerforce.common.tenant.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.trackerforce.common.config.RequestHeader;
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
	
	private final IdentityService identityService;
	
	public TenantInterceptor(IdentityService identityService) {
		this.identityService = identityService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (isHealthChecker(request))
			return true;
		
		var tenant = Optional.ofNullable(request.getHeader(
				RequestHeader.TENANT_HEADER.toString()));
		
		if (tenant.isPresent() && StringUtils.hasText(tenant.get())) {
			request.setAttribute(TENANT_ID, tenant.get());
			
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
	
	private boolean isHealthChecker(HttpServletRequest request) {
		return request.getRequestURI().equals(identityService.getHealthChecker());
	}
	
}
