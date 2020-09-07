package com.trackerforce.management.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.trackerforce.common.tenant.repository.AbstractProjectedDao;
import com.trackerforce.management.model.Procedure;
import com.trackerforce.management.model.Task;

@Repository
public class TaskRepositoryDao extends AbstractProjectedDao<Task> {
	
	@Autowired
	private TaskRerpository taskRepository;
	
	@Autowired
	private ProcedureRepository procedureRepository;
	
	@Autowired
	private ComponentRepository componentRepository;
	
	public void delete(final Task task) {
		disassociateTaskProcedureByTaskId(task.getId());
		taskRepository.delete(task);
	}
	
	public void deleteById(final String id) {
		disassociateTaskProcedureByTaskId(id);
		taskRepository.deleteById(id);
	}
	
	public Task save(final Task task) {
		return this.taskRepository.save(task);
	}
	
	public void disassociateTaskProcedureByTaskId(final String id) {
		final Query query = new Query();
		query.addCriteria(Criteria.where("childTasks").in(id));
		
		final List<Procedure> parentProcedures = mongoTemplate.find(query, Procedure.class);
		for (Procedure procedure : parentProcedures) {
			procedure.getChildTasks().removeIf(t -> t.getId().equals(id));
		}
		
		procedureRepository.saveAll(parentProcedures);
	}

	public ComponentRepository getComponentRepository() {
		return componentRepository;
	}
	
}
