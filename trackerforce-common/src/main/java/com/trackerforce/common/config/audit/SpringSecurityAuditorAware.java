package com.trackerforce.common.config.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	public Optional<String> getCurrentAuditor() {
		final String credential = SecurityContextHolder.getContext().getAuthentication().getName();
		return Optional.of(credential);
	}
}