package com.trackerforce.management.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.model.Template;
import org.springframework.stereotype.Repository;

@Repository
public class AgentRepositoryDao extends AbstractProjectedDao<Agent, AgentRepository> {

	private final AgentRepository agentRepository;

	public AgentRepositoryDao(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
	}
	
	public void delete(final Template procedure) {
		this.deleteById(procedure.getId());
	}
	
	public void deleteById(final String id) {
		agentRepository.deleteById(id);
	}

	@Override
	public AgentRepository getRepository() {
		return agentRepository;
	}
	
}
