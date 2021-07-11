package com.trackerforce.management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Task;
import com.trackerforce.management.model.request.ProcedureRequest;
import com.trackerforce.management.service.ProcedureService;

@CrossOrigin(allowedHeaders = { "X-Tenant" })
@RestController
@RequestMapping("management/procedure")
public class ProcedureController {

	@Autowired
	private ProcedureService procedureService;

	@PostMapping(value = "/v1/create")
	public ResponseEntity<?> create(@RequestBody ProcedureRequest procedureRequest) {
		try {
			return ResponseEntity.ok(procedureService.create(procedureRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PatchMapping(value = "/v1/{id}")
	public ResponseEntity<?> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		try {
			return ResponseEntity.ok(procedureService.update(id, updates));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PostMapping(value = "/v1/{id}/task/add/{taskId}")
	public ResponseEntity<Task> addTask(
			@PathVariable(value="id") String id,
			@PathVariable(value="taskId") String taskId) {
		return ResponseEntity.ok(procedureService.updateTasks(id, taskId, true));
	}
	
	@PostMapping(value = "/v1/{id}/task/remove/{taskId}")
	public ResponseEntity<Task> removeTask(
			@PathVariable(value="id") String id,
			@PathVariable(value="taskId") String taskId) {
		return ResponseEntity.ok(procedureService.updateTasks(id, taskId, false));
	}
	
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable(value="id") String id) {
		procedureService.delete(id);
		return ResponseEntity.ok().build();
	}
	
}
