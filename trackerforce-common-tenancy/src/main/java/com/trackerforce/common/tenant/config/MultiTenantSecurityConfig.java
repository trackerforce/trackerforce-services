package com.trackerforce.common.tenant.config;

import com.trackerforce.common.config.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MultiTenantSecurityConfig extends SecurityConfig {

	private static final String[] SWAGGER_MATCHERS = {
			"/v3/api-docs/**",
			"/swagger-ui/**",
			"/swagger-ui.html",
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth ->
				auth.requestMatchers(allowedEndpoint).permitAll()
					.requestMatchers(SWAGGER_MATCHERS).permitAll()
					.requestMatchers("/**").access(this::authorize));

		http.exceptionHandling(auth -> auth.authenticationEntryPoint(jwtAuthenticationEntryPoint));
		http.sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(AbstractHttpConfigurer::disable);

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
