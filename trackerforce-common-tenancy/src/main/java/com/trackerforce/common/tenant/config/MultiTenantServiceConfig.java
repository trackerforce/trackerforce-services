package com.trackerforce.common.tenant.config;

import com.trackerforce.common.tenant.interceptor.TenantInterceptor;
import com.trackerforce.common.tenant.service.IdentityService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MultiTenantServiceConfig implements WebMvcConfigurer  {

	private final IdentityService identityService;

	public MultiTenantServiceConfig(IdentityService identityService) {
		this.identityService = identityService;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TenantInterceptor(identityService));
	}

}
