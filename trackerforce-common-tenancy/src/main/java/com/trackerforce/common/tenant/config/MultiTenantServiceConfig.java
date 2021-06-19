package com.trackerforce.common.tenant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trackerforce.common.tenant.interceptor.TenantInterceptor;
import com.trackerforce.common.tenant.service.IdentityService;

@Configuration
public class MultiTenantServiceConfig implements WebMvcConfigurer  {
	
	@Autowired
	private IdentityService identityService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TenantInterceptor(identityService));
	}

}
