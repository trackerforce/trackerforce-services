package com.trackerforce.management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.model.Template;

@Repository
public class AgentRepositoryDao extends AbstractProjectedDao<Agent> {
	
	@Autowired
	private AgentRepository agentRepository;
	
	public void delete(final Template procedure) {
		this.deleteById(procedure.getId());
	}
	
	public void deleteById(final String id) {
		agentRepository.deleteById(id);
	}

	public AgentRepository getAgentRepository() {
		return agentRepository;
	}
	
}
