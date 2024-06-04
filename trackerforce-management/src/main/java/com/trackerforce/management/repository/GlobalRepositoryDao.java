package com.trackerforce.management.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Global;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GlobalRepositoryDao extends AbstractProjectedDao<Global, GlobalRepository> {

	private final GlobalRepository globalRepository;

	public GlobalRepositoryDao(MongoTemplate mongoTemplate, GlobalRepository globalRepository) {
		super(mongoTemplate);
		this.globalRepository = globalRepository;
	}

	@Override
	public GlobalRepository getRepository() {
		return globalRepository;
	}
	
}
