package com.trackerforce.management.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Template;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateRepositoryDao extends AbstractProjectedDao<Template, TemplateRepository> {

	private final TemplateRepository templateRepository;

	public TemplateRepositoryDao(TemplateRepository templateRepository) {
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
