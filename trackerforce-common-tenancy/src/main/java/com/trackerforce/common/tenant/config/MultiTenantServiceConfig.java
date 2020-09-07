package com.trackerforce.common.tenant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trackerforce.common.tenant.interceptor.TenantInterceptor;

@Configuration
public class MultiTenantServiceConfig implements WebMvcConfigurer  {  

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TenantInterceptor());
	}

}
