package com.trackerforce.management.controller;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.TaskRequest;
import com.trackerforce.management.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("management/task/v1")
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<Task> create(@RequestBody TaskRequest taskRequest) {
		return ResponseEntity.ok(taskService.create(taskRequest));
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<Map<String, Object>> findAll(
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		var query = new HashMap<String, Object>();
		query.put("description", description);
		
		var queryable = new QueryableRequest(query, sortBy, output, page, size);
		return ResponseEntity.ok(taskService.findAllProjectedBy(queryable));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Task> findOne(@PathVariable(value="id") String id, String output) {
		return ResponseEntity.ok(taskService.findByIdProjectedBy(id, output));
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Task> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		return ResponseEntity.ok(taskService.update(id, updates));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable(value="id") String id) {
		taskService.delete(id);
		return ResponseEntity.ok().build();
	}

}
