package com.trackerforce.management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Procedure;

@Repository
public class ProcedureRepositoryDao extends AbstractProjectedDao<Procedure, ProcedureRepository> {
	
	@Autowired
	private ProcedureRepository procedureRepository;
	
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
