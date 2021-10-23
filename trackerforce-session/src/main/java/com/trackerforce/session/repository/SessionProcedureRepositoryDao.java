package com.trackerforce.session.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.session.model.SessionProcedure;

@Repository
public class SessionProcedureRepositoryDao extends AbstractProjectedDao<SessionProcedure, SessionProcedureRepository> {
	
	@Autowired
	private SessionProcedureRepository sessionProcedureRepository;

	@Override
	public SessionProcedureRepository getRepository() {
		return sessionProcedureRepository;
	}

}
