package com.trackerforce.management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Hook {

	private String resolverUri;

	public String getResolverUri() {
		return resolverUri;
	}

	public void setResolverUri(String resolverUri) {
		this.resolverUri = resolverUri;
	}

}
