package com.trackerforce.management.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.repository.AgentRepositoryDao;

@Service
public class AgentService extends AbstractBusinessService<Agent> {

	private final AgentRepositoryDao agentDao;

	private final BCryptPasswordEncoder bcryptEncoder;

	public AgentService(AgentRepositoryDao agentDao, JwtTokenService jwtTokenUtil) {
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
		var optgent = agentDao.getAgentRepository().findById(agentId);

		if (!optgent.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		var agent = optgent.get();
		agent.getCases().add(caseId);
		agent = agentDao.save(agent);

		return AgentResponse.watch(agent.getEmail(), agent.getCases());
	}

	public AgentResponse unWatchCase(String agentId, String caseId) {
		var optgent = agentDao.getAgentRepository().findById(agentId);

		if (!optgent.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		var agent = optgent.get();
		agent.getCases().remove(caseId);
		agent = agentDao.save(agent);

		return AgentResponse.watch(agent.getEmail(), agent.getCases());
	}

	public AgentResponse create(HttpServletRequest request, AgentRequest agentRequest) throws ServiceException {
		final var password = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
		agentRequest.setPassword(bcryptEncoder.encode(password));

		final var agent = super.create(new Agent(agentRequest));
		var agentResponse = AgentResponse.createAgent(agent.getName(), agent.getEmail(), password, agent.getRoles());
		return agentResponse;
	}

	public AgentResponse activate(final AgentRequest agentRequest) {
		var agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());
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
			agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());

			if (!agent.isActive())
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		if (!bcryptEncoder.matches(agentRequest.getPassword(), agent.getPassword()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		agent.setOnline(true);
		agentDao.save(agent);

		return AgentResponse.login(agent.getId(), agent.getEmail(), agent.getRoles(), agent.isOnline(),
				agent.isActive());
	}

	public void logoff(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var agent = agentDao.getAgentRepository().findById(authentication.getName()).get();

		if (!agent.isActive() || !agent.isOnline())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		agent.setOnline(false);
		agentDao.save(agent);
	}

	public boolean isOnline(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var optAgent = agentDao.getAgentRepository().findById(authentication.getName());

		if (!optAgent.isPresent())
			return false;

		return optAgent.get().isOnline() && optAgent.get().isActive();
	}

	public Agent getAuthenticated(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var optAgent = agentDao.getAgentRepository().findById(authentication.getName());

		if (!optAgent.isPresent())
			return null;

		return optAgent.get();
	}

}
