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
		agentResponse.setRoles(agent.getRoles());
		agentResponse.setTempAccess(password);

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

		var agentResponse = new AgentResponse();
		agentResponse.setEmail(agent.getEmail());
		agentResponse.setDepartment(agent.getDepartment());
		agentResponse.setRoles(agent.getRoles());
		agentResponse.setOnline(agent.isOnline());
		agentResponse.setActive(agent.isActive());

		return agentResponse;
	}

	public void logoff(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var agent = agentDao.getAgentRepository().findByEmail(authentication.getName());

		if (!agent.isActive() || !agent.isOnline())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		agent.setOnline(false);
		agentDao.save(agent);
	}

	public boolean isOnline(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var agentRequest = new AgentRequest();

		agentRequest.setEmail(authentication.getName());
		final var agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());;
		return agent.isOnline() && agent.isActive();
	}

	public Agent getAuthenticated(HttpServletRequest request) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		return agentDao.getAgentRepository().findByEmail(authentication.getName());
	}

}
