package com.trackerforce.common.model.request;

import java.util.Map;

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

	public Map<String, Object> getQuery() {
		return query;
	}

	public void setQuery(Map<String, Object> query) {
		this.query = query;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
