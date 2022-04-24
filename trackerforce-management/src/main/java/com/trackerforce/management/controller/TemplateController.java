package com.trackerforce.management.controller;

import java.util.HashMap;
import java.util.LinkedList;
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
import com.trackerforce.management.model.Procedure;
import com.trackerforce.management.model.request.TemplateRequest;
import com.trackerforce.management.service.TemplateService;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("management/template/v1")
public class TemplateController {

	@Autowired
	private TemplateService templateService;

	@PostMapping(value = "/create")
	public ResponseEntity<?> create(@RequestBody TemplateRequest templateRequest) {
		try {
			return ResponseEntity.ok(templateService.create(templateRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
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
		return ResponseEntity.ok(templateService.findAllProjectedBy(queryable));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findOne(@PathVariable(value="id") String id, String output) {
		return ResponseEntity.ok(templateService.findByIdProjectedBy(id, output));
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<?> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		try {
			return ResponseEntity.ok(templateService.update(id, updates));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PostMapping(value = "/{id}/procedure/reorder")
	public ResponseEntity<LinkedList<Procedure>> reorderProcedure(
			@PathVariable(value="id") String id,
			@RequestParam(required = true) int from,
			@RequestParam(required = true) int to) {
		return ResponseEntity.ok(templateService.reorderProcedure(id, from, to));
	}
	
	@PostMapping(value = "/{id}/procedure/add/{procedureId}")
	public ResponseEntity<Procedure> addProcedure(
			@PathVariable(value="id") String id,
			@PathVariable(value="procedureId") String procedureId) {
		return ResponseEntity.ok(templateService.updateProcedures(id, procedureId, true));
	}
	
	@PostMapping(value = "/{id}/procedure/remove/{procedureId}")
	public ResponseEntity<Procedure> removeProcedure(
			@PathVariable(value="id") String id,
			@PathVariable(value="procedureId") String procedureId) {
		return ResponseEntity.ok(templateService.updateProcedures(id, procedureId, false));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value="id") String id) {
		templateService.delete(id);
		return ResponseEntity.ok().build();
	}
	
}
