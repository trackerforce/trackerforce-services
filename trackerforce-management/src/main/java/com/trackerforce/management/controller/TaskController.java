package com.trackerforce.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackerforce.common.model.response.ErrorResponse;
import com.trackerforce.common.service.exception.ServiceException;
import com.trackerforce.management.model.Task;
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
			final Task task = taskService.createTask(
					taskRequest.getTask().getDescription(), taskRequest.getTask().getType(), 
					taskRequest.getHelper());

			return ResponseEntity.ok(task);
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping(value = "/v1/find")
	public ResponseEntity<?> create(@RequestParam String id, String output) {
		return ResponseEntity.ok(taskService.findByIdProjectedBy(id, output));
	}

}
