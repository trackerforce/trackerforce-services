package com.trackerforce.management.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.repository.AgentRepositoryDao;

@Service
public class AgentService extends AbstractBusinessService<Agent> {

	private AgentRepositoryDao agentDao;
	
	private BCryptPasswordEncoder bcryptEncoder;
	
	public AgentService(
			AgentRepositoryDao agentDao) {
		super(agentDao, Agent.class, "agent");
		this.agentDao = agentDao;
		this.bcryptEncoder = new BCryptPasswordEncoder();
	}

	@Override
	protected void validate(final Agent entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.hasText(entity.getName(), "'name' must not be empty");
			Assert.hasText(entity.getEmail(), "'email' must not be empty");
			Assert.hasText(entity.getDepartment(), "'department' must not be empty");
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public AgentResponse create(final AgentRequest agentRequest) throws ServiceException {
		final var password = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
		agentRequest.setPassword(bcryptEncoder.encode(password));
		
		final var agent = super.create(new Agent(agentRequest));
		var agentResponse = new AgentResponse();
		agentResponse.setName(agent.getName());
		agentResponse.setEmail(agent.getEmail());
		agentResponse.setDepartment(agent.getDepartment());
		agentResponse.setTemp_access(password);
		
		return agentResponse;
	}
	
	public AgentResponse findAgent(final AgentRequest agentRequest) {
		var agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());
		
		var agentResponse = new AgentResponse();
		agentResponse.setName(agent.getName());
		agentResponse.setEmail(agent.getEmail());
		agentResponse.setDepartment(agent.getDepartment());
		
		return agentResponse;
	}
	
	public void activate(final AgentRequest agentRequest) {
		var agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());
		agent.setActive(true);
		agentDao.save(agent);
	}

}
