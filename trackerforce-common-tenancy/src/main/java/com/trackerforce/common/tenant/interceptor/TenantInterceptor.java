package com.trackerforce.common.tenant.interceptor;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.trackerforce.common.model.type.RequestHeader;
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
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		
		var allowedEndpoint = isAllowedEndpoint(request);
		var tenant = Optional.ofNullable(request.getHeader(RequestHeader.TENANT_HEADER.toString()));
		
		if (tenant.isPresent() && StringUtils.hasText(tenant.get())) {
			request.setAttribute(TENANT_ID, tenant.get());
			
			if (!allowedEndpoint && !identityService.validateIdentity(request)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
		} else if (!allowedEndpoint) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	private boolean isAllowedEndpoint(HttpServletRequest request) {
		return Arrays.stream(identityService.getAllowedEndpoints())
			.anyMatch(endpoint -> request.getRequestURI().matches(endpoint));
	}
	
}
