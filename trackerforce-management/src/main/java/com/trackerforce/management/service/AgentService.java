package com.trackerforce.management.service;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.repository.AgentRepository;
import com.trackerforce.management.repository.AgentRepositoryDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AgentService extends AbstractBusinessService<Agent, AgentRepository> {

	private final AgentRepositoryDao agentDao;

	private final BCryptPasswordEncoder bcryptEncoder;

	public AgentService(AgentRepositoryDao agentDao) {
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
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public AgentResponse watchCase(String agentId, String caseId) {
		var optAgent = agentDao.getRepository().findById(agentId);

		if (optAgent.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		var agent = optAgent.get();
		
		if (!agent.getCases().contains(caseId)) {
			agent.getCases().add(caseId);
			agent = agentDao.save(agent);			
		}

		return AgentResponse.watch(agent.getEmail(), agent.getCases());
	}

	public AgentResponse unWatchCase(String agentId, String caseId) {
		var optAgent = agentDao.getRepository().findById(agentId);

		if (optAgent.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		var agent = optAgent.get();
		agent.getCases().remove(caseId);
		agent = agentDao.save(agent);

		return AgentResponse.watch(agent.getEmail(), agent.getCases());
	}

	public AgentResponse create(AgentRequest agentRequest) {
		try {
			final var password = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
			agentRequest.setPassword(bcryptEncoder.encode(password));

			final var agent = super.create(new Agent(agentRequest));
			return AgentResponse.createAgent(agent.getName(), agent.getEmail(), password, agent.getRoles());
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	public AgentResponse activate(final AgentRequest agentRequest) {
		var agent = agentDao.getRepository().findByEmail(agentRequest.getEmail());
		var agentResponse = login(agentRequest, agent);

		agent.setPassword(bcryptEncoder.encode(agentRequest.getNewPassword()));
		agent.setActive(true);
		agent = agentDao.save(agent);

		agentResponse.setTempAccess(bcryptEncoder.encode(agentRequest.getNewPassword()));
		agentResponse.setActive(agent.isActive());
		return agentResponse;
	}

	public AgentResponse login(final AgentRequest agentRequest, Agent agent) {
		if (agent == null) {
			agent = agentDao.getRepository().findByEmail(agentRequest.getEmail());

			if (agent == null || !agent.isActive())
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		if (!bcryptEncoder.matches(agentRequest.getPassword(), agent.getPassword()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		agent.setOnline(true);
		agentDao.save(agent);

		return AgentResponse.login(agent.getId(), agent.getEmail(), agent.getRoles());
	}

	public void logoff() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var optAgent = agentDao.getRepository().findById(authentication.getName());

		if (optAgent.isEmpty())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		var agent = optAgent.get();
		if (!agent.isActive() || !agent.isOnline())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		agent.setOnline(false);
		agentDao.save(agent);
	}

	public boolean isOnline() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var optAgent = agentDao.getRepository().findById(authentication.getName());

        return optAgent.filter(agent -> agent.isOnline() && agent.isActive()).isPresent();
    }

	public Agent getAuthenticated() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var optAgent = agentDao.getRepository().findById(authentication.getName());

        return optAgent.orElse(null);
    }

}
