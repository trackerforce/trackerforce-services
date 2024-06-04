package com.trackerforce.session.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.session.model.SessionCase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionCaseRepositoryDao extends AbstractProjectedDao<SessionCase, SessionCaseRepository> {

    private final SessionCaseRepository caseRepository;

    public SessionCaseRepositoryDao(MongoTemplate mongoTemplate, SessionCaseRepository caseRepository) {
        super(mongoTemplate);
        this.caseRepository = caseRepository;
    }

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
