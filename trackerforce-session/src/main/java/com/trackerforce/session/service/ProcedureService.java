package com.trackerforce.session.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.repository.SessionCaseRepositoryDao;

@Service
public class ProcedureService extends AbstractSessionService<SessionCase> {

	private final SessionCaseRepositoryDao sessionCaseDao;
	
	public ProcedureService(
			SessionCaseRepositoryDao procedureDao) {
		this.sessionCaseDao = procedureDao;
	}

	@Override
	protected void validate(final SessionCase entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public SessionCase create(final SessionCaseRequest procedureRequest) throws ServiceException {
		//TODO Create new sessionCase
		return null;
	}
	
}
