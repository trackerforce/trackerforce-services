package com.trackerforce.session.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.SessionTask;
import com.trackerforce.session.model.request.SessionProcedureRequest;
import com.trackerforce.session.repository.SessionCaseRepositoryDao;

@Service
public class SessionProcedureService extends AbstractSessionService<SessionProcedure> {

	private final SessionCaseRepositoryDao sessionCaseDao;

	private final ManagementService managementService;

	public SessionProcedureService(SessionCaseRepositoryDao sessionCaseDao, ManagementService managementService) {
		this.sessionCaseDao = sessionCaseDao;
		this.managementService = managementService;
	}

	@Override
	protected void validate(final SessionProcedure entity) throws ServiceException {
		try {
			Assert.notNull(entity, "The class must not be null");
			Assert.notEmpty(entity.getTaskResolution(), "Procedure has no tasks");
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public SessionProcedure hanlder(HttpServletRequest request, final SessionProcedureRequest sessionProcedureRequest)
			throws ServiceException {
		switch (sessionProcedureRequest.getEvent()) {
		case NEW:
			return create(request, sessionProcedureRequest);
		case SAVE:
			return save(sessionProcedureRequest);
		default:
			throw new ServiceException("Invalid Session Procedure Event");
		}
	}

	private SessionProcedure create(HttpServletRequest request, final SessionProcedureRequest sessionProcedureRequest)
			throws ServiceException {
		var sessionCase = getSessionCase(sessionProcedureRequest.getSessionCaseId());
		var commonProcedure = managementService.findProcedure(request, sessionProcedureRequest.getProcedureId());
		var procedure = SessionProcedure.create(commonProcedure);

		validate(procedure);
		
		sessionCase.getProcedures().add(procedure);
		sessionCaseDao.save(sessionCase);
		return procedure;
	}

	private SessionProcedure save(final SessionProcedureRequest sessionProcedureRequest) {
		var sessionCase = getSessionCase(sessionProcedureRequest.getSessionCaseId());
		var procedure = getSessionProcedure(sessionCase, sessionProcedureRequest.getProcedureId());
		for (SessionTask task : sessionProcedureRequest.getTasks()) {
			var optTask = procedure.getTaskResolution().stream().filter(t -> t.getId().equals(task.getId()))
					.findFirst();

			if (optTask.isPresent())
				optTask.get().setResponse(task.getResponse());
		}

		sessionCaseDao.save(sessionCase);
		return procedure;
	}

	private SessionCase getSessionCase(String sessionCaseId) {
		var optCase = sessionCaseDao.getCaseRepository().findById(sessionCaseId);

		if (!optCase.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found");

		return optCase.get();
	}

	private SessionProcedure getSessionProcedure(SessionCase sessionCase, String sessionProcedureId) {
		var optProcedure = sessionCase.getProcedures().stream().filter(p -> p.getId().equals(sessionProcedureId))
				.findFirst();

		if (!optProcedure.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedure not found");

		return optProcedure.get();
	}

}
