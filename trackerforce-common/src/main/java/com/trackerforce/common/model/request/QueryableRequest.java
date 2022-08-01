package com.trackerforce.common.model.request;

import java.util.Map;

import lombok.Data;

@Data
public class QueryableRequest {

	private Map<String, Object> query;

	private String sortBy;

	private String output;

	private int page;

	private int size;

	public QueryableRequest(Map<String, Object> query, 
			String sortBy, String output,
			int page, int size) {
		this.query = query;
		this.sortBy = sortBy;
		this.output = output;
		this.page = page;
		this.size = size;
	}
	
	public QueryableRequest(Map<String, Object> query,
			int page, int size) {
		this.query = query;
		this.page = page;
		this.size = size;
	}

}
