package com.trackerforce.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.request.TaskRequest;
import com.trackerforce.management.service.TaskService;

@CrossOrigin(allowedHeaders = { "X-Tenant" })
@RestController
@RequestMapping("management/task")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@PostMapping(value = "/v1/create")
	public ResponseEntity<?> create(@RequestBody TaskRequest taskRequest) {
		try {
			return ResponseEntity.ok(taskService.create(taskRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping(value = "/v1/")
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
	
	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<?> findOne(@PathVariable(value="id") String id, String output) {
		return ResponseEntity.ok(taskService.findByIdProjectedBy(id, output));
	}
	
	@PatchMapping(value = "/v1/{id}")
	public ResponseEntity<?> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		try {
			return ResponseEntity.ok(taskService.update(id, updates));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable(value="id") String id) {
		taskService.delete(id);
		return ResponseEntity.ok().build();
	}

}
