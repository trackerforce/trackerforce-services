package com.trackerforce.session.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.session.model.SessionCase;

@Repository
public class SessionCaseRepositoryDao extends AbstractProjectedDao<SessionCase, SessionCaseRepository> {

	@Autowired
	private SessionCaseRepository caseRepository;

	public void delete(final SessionCase sessionCase) {
		this.deleteById(sessionCase.getId());
	}

	public void deleteById(final String id) {
		caseRepository.deleteById(id);
	}

	@Override
	public SessionCaseRepository getRepository() {
		return caseRepository;
	}

}
