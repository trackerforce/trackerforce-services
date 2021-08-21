package com.trackerforce.session.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.repository.SessionCaseRepositoryDao;

@Service
public class SessionProcedureService extends AbstractSessionService<SessionProcedure> {

	private final SessionCaseRepositoryDao sessionCaseDao;

	private final ManagementService managementService;

	public SessionProcedureService(SessionCaseRepositoryDao procedureDao, ManagementService managementService) {
		this.sessionCaseDao = procedureDao;
		this.managementService = managementService;
	}

	@Override
	protected void validate(final SessionProcedure entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public SessionCase create(HttpServletRequest request, final SessionCaseRequest sessionCaseRequest)
			throws ServiceException {

		var template = managementService.findTemplate(request, sessionCaseRequest.getTemplate());
		var sessionCase = SessionCase.create(template);

//		this.validate(sessionCase);
		return sessionCaseDao.save(sessionCase);
	}

}
