package com.trackerforce.management.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Global;
import org.springframework.stereotype.Repository;

@Repository
public class GlobalRepositoryDao extends AbstractProjectedDao<Global, GlobalRepository> {

	private final GlobalRepository globalRepository;

	public GlobalRepositoryDao(GlobalRepository globalRepository) {
		this.globalRepository = globalRepository;
	}

	@Override
	public GlobalRepository getRepository() {
		return globalRepository;
	}
	
}
