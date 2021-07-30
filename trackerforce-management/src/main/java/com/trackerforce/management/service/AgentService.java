package com.trackerforce.management.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.config.JwtRequestFilter;
import com.trackerforce.common.config.RequestHeader;
import com.trackerforce.common.model.request.AgentRequest;
import com.trackerforce.common.model.response.AgentResponse;
import com.trackerforce.common.service.JwtTokenService;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Agent;
import com.trackerforce.management.repository.AgentRepositoryDao;

import io.jsonwebtoken.Claims;

@Service
public class AgentService extends AbstractBusinessService<Agent> {

	private final AgentRepositoryDao agentDao;
	
	private final BCryptPasswordEncoder bcryptEncoder;
	
	private final JwtTokenService jwtTokenUtil;
	
	public AgentService(
			AgentRepositoryDao agentDao,
			JwtTokenService jwtTokenUtil) {
		super(agentDao, Agent.class, "agent");
		this.agentDao = agentDao;
		this.jwtTokenUtil = jwtTokenUtil;
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
	
	public AgentResponse findAgent(final AgentRequest agentRequest) {
		var agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());
		
		var agentResponse = new AgentResponse();
		agentResponse.setName(agent.getName());
		agentResponse.setEmail(agent.getEmail());
		agentResponse.setDepartment(agent.getDepartment());
		
		return agentResponse;
	}
	
	public AgentResponse activate(final AgentRequest agentRequest) {
		var agent = agentDao.getAgentRepository().findByEmail(agentRequest.getEmail());
		
		if (agent.isActive() || 
				!bcryptEncoder.matches(agentRequest.getPassword(), agent.getPassword()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); 
		
		agent.setPassword(bcryptEncoder.encode(agentRequest.getNewPassword()));
		agent.setActive(true);
		agentDao.save(agent);
		
		var agentResponse = new AgentResponse();
		agentResponse.setEmail(agent.getEmail());
		agentResponse.setTempAccess(bcryptEncoder.encode(agentRequest.getNewPassword()));
		agentResponse.setDepartment(agent.getDepartment());
		agentResponse.setRoles(agent.getRoles());
		
		return agentResponse;
	}
	
	public AgentResponse getAuthenticated(HttpServletRequest request) throws ServiceException {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		
		var token = JwtRequestFilter.getJwtFromRequest(request);
		if (!token.isPresent())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		var roles = jwtTokenUtil.getClaimFromToken(token.get(), claims -> claims.get("roles", List.class));
		var agentRequest = new AgentRequest();
		agentRequest.setEmail(authentication.getName());
		
		var authAccess = findAgent(agentRequest);
		if (authAccess == null || !authentication.isAuthenticated() || !roles.contains("AGENT"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		var tenant = request.getHeader(RequestHeader.TENANT_HEADER.toString());
		var orgAlias = jwtTokenUtil.getClaimFromToken(token.get(), Claims::getAudience);
		if (!StringUtils.hasText(orgAlias) || !orgAlias.equals(tenant))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		
		return authAccess;
	}

}
