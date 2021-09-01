package com.trackerforce.session.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.trackerforce.common.model.exception.BusinessException;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.session.model.SessionCase;
import com.trackerforce.session.model.SessionProcedure;
import com.trackerforce.session.model.SessionTask;
import com.trackerforce.session.model.request.SessionCaseRequest;
import com.trackerforce.session.model.request.SessionProcedureRequest;
import com.trackerforce.session.model.type.ProcedureStatus;
import com.trackerforce.session.repository.SessionCaseRepositoryDao;

@Service
public class SessionCaseService extends AbstractSessionService<SessionCase> {

	private final SessionCaseRepositoryDao sessionCaseDao;

	private final ManagementService managementService;

	private final QueueService queueService;

	public SessionCaseService(SessionCaseRepositoryDao sessionCaseDao, ManagementService managementService,
			QueueService queueService) {
		super(sessionCaseDao, SessionCase.class);
		this.sessionCaseDao = sessionCaseDao;
		this.managementService = managementService;
		this.queueService = queueService;
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

	public SessionProcedure handlerProcedure(HttpServletRequest request,
			final SessionProcedureRequest sessionProcedureRequest) throws ServiceException {
		switch (sessionProcedureRequest.getEvent()) {
		case NEW:
			return createProcedure(request, sessionProcedureRequest);
		case SUBMIT:
			return submitProcedure(request, sessionProcedureRequest);
		case SAVE:
			return saveProcedure(sessionProcedureRequest);
		case CANCEL:
			return cancelProcedure(sessionProcedureRequest);
		default:
			throw new ServiceException("Invalid Session Procedure Event");
		}
	}

	private SessionProcedure createProcedure(HttpServletRequest request,
			final SessionProcedureRequest sessionProcedureRequest) throws ServiceException {
		var sessionCase = getSessionCase(sessionProcedureRequest.getSessionCaseId());
		var commonProcedure = managementService.findProcedure(request, sessionProcedureRequest.getProcedureId());
		var procedure = SessionProcedure.create(commonProcedure);

		sessionCase.getProcedures().add(procedure);
		sessionCaseDao.save(sessionCase);
		return procedure;
	}

	private SessionProcedure submitProcedure(HttpServletRequest request,
			final SessionProcedureRequest sessionProcedureRequest) throws ServiceException {
		var sessionCase = getSessionCase(sessionProcedureRequest.getSessionCaseId());
		var procedure = getSessionProcedure(sessionCase, sessionProcedureRequest.getProcedureId());

		try {
			procedure.changeStatus(ProcedureStatus.SUBMITTED);
			sessionCaseDao.save(sessionCase);

			queueService.submitProcedure(request, procedure, sessionCase.getContextId());
			return procedure;
		} catch (BusinessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	private SessionProcedure saveProcedure(final SessionProcedureRequest sessionProcedureRequest) {
		var sessionCase = getSessionCase(sessionProcedureRequest.getSessionCaseId());
		var procedure = getSessionProcedure(sessionCase, sessionProcedureRequest.getProcedureId());

		for (SessionTask task : sessionProcedureRequest.getTasks()) {
			var optTask = procedure.getTasks().stream().filter(t -> t.getId().equals(task.getId())).findFirst();

			if (optTask.isPresent())
				optTask.get().setResponse(task.getResponse());
		}

		sessionCaseDao.save(sessionCase);
		return procedure;
	}

	private SessionProcedure cancelProcedure(final SessionProcedureRequest sessionProcedureRequest) {
		var sessionCase = getSessionCase(sessionProcedureRequest.getSessionCaseId());
		var procedure = getSessionProcedure(sessionCase, sessionProcedureRequest.getProcedureId());

		try {
			procedure.changeStatus(ProcedureStatus.CANCELED);
			sessionCaseDao.save(sessionCase);
			return procedure;
		} catch (BusinessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
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
