package com.trackerforce.management.repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Procedure;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProcedureRepositoryDao extends AbstractProjectedDao<Procedure, ProcedureRepository> {

	private final ProcedureRepository procedureRepository;

	public ProcedureRepositoryDao(MongoTemplate mongoTemplate, ProcedureRepository procedureRepository) {
		super(mongoTemplate);
		this.procedureRepository = procedureRepository;
	}
	
	public void delete(final Procedure procedure) {
		this.deleteById(procedure.getId());
	}
	
	public void deleteById(final String id) {
		procedureRepository.deleteById(id);
	}

	@Override
	public ProcedureRepository getRepository() {
		return procedureRepository;
	}
	
}
