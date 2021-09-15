package com.trackerforce.management.controller;

import java.util.HashMap;
import java.util.List;
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
import com.trackerforce.management.model.request.GlobalRequest;
import com.trackerforce.management.service.GlobalService;

@CrossOrigin(allowedHeaders = { "X-Tenant" })
@RestController
@RequestMapping("management/global")
public class GlobalController {

	@Autowired
	private GlobalService globalService;

	@PostMapping(value = "/v1/create")
	public ResponseEntity<?> create(@RequestBody GlobalRequest globalRequest) {
		try {
			return ResponseEntity.ok(globalService.create(globalRequest));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping(value = "/v1/")
	public ResponseEntity<Map<String, Object>> findAll(
			@RequestBody(required = true) Map<String, Object> query,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		var queryable = new QueryableRequest(query, sortBy, output, page, size);
		return ResponseEntity.ok(globalService.findAllProjectedBy(queryable));
	}
	
	@GetMapping(value = "/v1/{key}")
	public ResponseEntity<?> findOneByKey(@PathVariable(value="key") String key, String output) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("key", key);
		
		var queryable = new QueryableRequest(query, null, output, 0, 1);
		var result = globalService.findAllProjectedBy(queryable);
		var data = ((List<?>) result.get("data"));
		
		if (data.size() == 0)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok( data.get(0));
	}
	
	@PatchMapping(value = "/v1/{id}")
	public ResponseEntity<?> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		try {
			return ResponseEntity.ok(globalService.update(id, updates));
		} catch (ServiceException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable(value="id") String id) {
		globalService.delete(id);
		return ResponseEntity.ok().build();
	}

}
