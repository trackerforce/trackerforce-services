package com.trackerforce.management.controller;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.management.model.Procedure;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.ProcedureRequest;
import com.trackerforce.management.service.ProcedureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("management/procedure/v1")
public class ProcedureController {

	private final ProcedureService procedureService;

	public ProcedureController(ProcedureService procedureService) {
		this.procedureService = procedureService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<Procedure> create(@RequestBody ProcedureRequest procedureRequest) {
		return ResponseEntity.ok(procedureService.create(procedureRequest));
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<Map<String, Object>> findAll(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		var query = new HashMap<String, Object>();
		query.put("description", description);
		query.put("name", name);
		
		var queryable = new QueryableRequest(query, sortBy, output, page, size);
		return ResponseEntity.ok(procedureService.findAllProjectedBy(queryable));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Procedure> findOne(
			@PathVariable(value = "id") String id,
			@RequestParam(required = false) String output) {
		return ResponseEntity.ok(procedureService.findByIdProjectedBy(id, output));
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Procedure> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		return ResponseEntity.ok(procedureService.update(id, updates));
	}
	
	@PostMapping(value = "/{id}/task/reorder")
	public ResponseEntity<List<Task>> reorderTask(
			@PathVariable(value="id") String id,
			@RequestParam(required = true) int from,
			@RequestParam(required = true) int to) {
		return ResponseEntity.ok(procedureService.reorderTask(id, from, to));
	}
	
	@PostMapping(value = "/{id}/task/add/{taskId}")
	public ResponseEntity<Task> addTask(
			@PathVariable(value="id") String id,
			@PathVariable(value="taskId") String taskId) {
		return ResponseEntity.ok(procedureService.updateTasks(id, taskId, true));
	}
	
	@PostMapping(value = "/{id}/task/remove/{taskId}")
	public ResponseEntity<Task> removeTask(
			@PathVariable(value="id") String id,
			@PathVariable(value="taskId") String taskId) {
		return ResponseEntity.ok(procedureService.updateTasks(id, taskId, false));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable(value="id") String id) {
		procedureService.delete(id);
		return ResponseEntity.ok().build();
	}
	
}
