package com.trackerforce.management.controller;

import com.trackerforce.common.model.request.QueryableRequest;
import com.trackerforce.common.tenant.model.type.GlobalKeyType;
import com.trackerforce.management.model.Global;
import com.trackerforce.management.model.request.GlobalRequest;
import com.trackerforce.management.model.response.GlobalResponse;
import com.trackerforce.management.service.GlobalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(allowedHeaders = { "X-Tenant", "Authorization", "Content-Type" })
@RestController
@RequestMapping("management/global/v1")
public class GlobalController {

	private final GlobalService globalService;

	public GlobalController(GlobalService globalService) {
		this.globalService = globalService;
	}

	@PostMapping(value = "/create")
	public ResponseEntity<Global> create(@RequestBody GlobalRequest globalRequest) {
		return ResponseEntity.ok(globalService.create(globalRequest));
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<Map<String, Object>> findAll(
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String key,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String output,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Map<String, Object> query = new HashMap<>();
		query.put("key", key);
		query.put("description", description);
		
		var queryable = new QueryableRequest(query, sortBy, output, page, size);
		return ResponseEntity.ok(globalService.findAllProjectedBy(queryable));
	}
	
	@GetMapping(value = "/list")
	public ResponseEntity<ArrayList<GlobalResponse>> listGlobals() {
		var keys = new ArrayList<GlobalResponse>();
		for (GlobalKeyType gk : GlobalKeyType.values())
			keys.add(new GlobalResponse(gk.name(), gk.getDescription(), gk.getAttributes()));

		return ResponseEntity.ok(keys);
	}
	
	@GetMapping(value = "/{key}")
	public ResponseEntity<Object> findOneByKey(@PathVariable(value="key") String key, String output) {
		Map<String, Object> query = new HashMap<>();
		query.put("key", key);
		
		var queryable = new QueryableRequest(query, null, output, 0, 1);
		var result = globalService.findAllProjectedBy(queryable);
		var data = ((List<?>) result.get("data"));
		
		if (data.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(data.getFirst());
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Global> update(
			@PathVariable(value="id") String id, 
			@RequestBody Map<String, Object> updates) {
		return ResponseEntity.ok(globalService.update(id, updates));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable(value="id") String id) {
		globalService.delete(id);
		return ResponseEntity.ok().build();
	}

}
