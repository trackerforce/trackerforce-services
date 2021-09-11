package com.trackerforce.management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Global;

@Repository
public class GlobalRepositoryDao extends AbstractProjectedDao<Global> {
	
	@Autowired
	private GlobalRepository globalRepository;

	public GlobalRepository getGlobalRepository() {
		return globalRepository;
	}
	
}
