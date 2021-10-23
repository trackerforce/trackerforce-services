package com.trackerforce.management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Template;

@Repository
public class TemplateRepositoryDao extends AbstractProjectedDao<Template, TemplateRepository> {
	
	@Autowired
	private TemplateRepository templateRepository;
	
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
