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
public class TaskRepositoryDao extends AbstractProjectedDao<Task, TaskRepository> {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private ProcedureRepository procedureRepository;
	
	/**
	 * {@link TaskRepositoryDao#deleteById(String)}
	 * 
	 * @param task Task
	 */
	public void delete(final Task task) {
		this.deleteById(task.getId());
	}
	
	/**
	 * Remove Task from all associated Procedures and then delete it.
	 * 
	 * @param id Task Id
	 */
	public void deleteById(final String id) {
		disassociateTaskProcedureByTaskId(id);
		taskRepository.deleteById(id);
	}
	
	public void disassociateTaskProcedureByTaskId(final String id) {
		final Query query = new Query();
		query.addCriteria(Criteria.where("childTasks").in(id));
		
		final List<Procedure> parentProcedures = mongoTemplate.find(query, Procedure.class);
		for (Procedure procedure : parentProcedures) {
			procedure.getTasks().removeIf(t -> t.getId().equals(id));
		}
		
		procedureRepository.saveAll(parentProcedures);
	}

	@Override
	public TaskRepository getRepository() {
		return taskRepository;
	}
	
}
