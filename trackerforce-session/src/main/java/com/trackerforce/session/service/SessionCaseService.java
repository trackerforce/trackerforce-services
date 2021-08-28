package com.trackerforce.session.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.repository.SessionCaseRepositoryDao;

@Service
public class SessionCaseService extends AbstractSessionService<SessionCase> {

	private final SessionCaseRepositoryDao sessionCaseDao;

	private final ManagementService managementService;

	public SessionCaseService(SessionCaseRepositoryDao sessionCaseDao, ManagementService managementService) {
		this.sessionCaseDao = sessionCaseDao;
		this.managementService = managementService;
	}

	@Override
	protected void validate(final SessionCase entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.notNull(entity.getProtocol(), "Protocol must not be null");
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public SessionCase create(HttpServletRequest request, final SessionCaseRequest sessionCaseRequest)
			throws ServiceException {

		var template = managementService.findTemplate(request, sessionCaseRequest.getTemplate());
		var sessionCase = SessionCase.create(template);

		this.validate(sessionCase);
		sessionCase = sessionCaseDao.save(sessionCase);
		managementService.watchCase(request, sessionCase.getId(), sessionCaseRequest.getAgentId());
		
		return sessionCase;
	}

}
