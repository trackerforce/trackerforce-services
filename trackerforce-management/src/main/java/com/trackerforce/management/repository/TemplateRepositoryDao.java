package com.trackerforce.management.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Template;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateRepositoryDao extends AbstractProjectedDao<Template, TemplateRepository> {

	private final TemplateRepository templateRepository;

	public TemplateRepositoryDao(MongoTemplate mongoTemplate, TemplateRepository templateRepository) {
		super(mongoTemplate);
		this.templateRepository = templateRepository;
	}
	
	public void delete(final Template procedure) {
		this.deleteById(procedure.getId());
	}
	
	public void deleteById(final String id) {
		templateRepository.deleteById(id);
	}

	@Override
	public TemplateRepository getRepository() {
		return templateRepository;
	}
	
}
