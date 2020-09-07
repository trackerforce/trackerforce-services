package com.trackerforce.common.tenant.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.trackerforce.common.config.audit.SpringSecurityAuditorAware;

@Configuration
@EnableMongoAuditing
class MongoConfigAuditor {

	@Bean
	public AuditorAware<String> initSpringAuditor() {
		return new SpringSecurityAuditorAware();
	}
}