package com.trackerforce.session.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.session.model.SessionProcedure;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionProcedureRepositoryDao extends AbstractProjectedDao<SessionProcedure, SessionProcedureRepository> {

    private final SessionProcedureRepository sessionProcedureRepository;

    public SessionProcedureRepositoryDao(MongoTemplate mongoTemplate, SessionProcedureRepository sessionProcedureRepository) {
        super(mongoTemplate);
        this.sessionProcedureRepository = sessionProcedureRepository;
    }

    @Override
    public SessionProcedureRepository getRepository() {
        return sessionProcedureRepository;
    }

}
